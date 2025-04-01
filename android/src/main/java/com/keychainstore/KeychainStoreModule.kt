package com.keychainstore

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule

@ReactModule(name = KeychainStoreModule.NAME)
class KeychainStoreModule(reactContext: ReactApplicationContext) :
  NativeKeychainStoreSpec(reactContext) {
    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
    private val transformation = "AES/GCM/NoPadding"
    private val keyCache = mutableMapOf<String, SecretKey>()

    override fun getName() = NAME

    override fun setItem(key: String?, `val`: String?): Boolean {
        return try {
            if (key.isNullOrEmpty() || `val`.isNullOrEmpty()) return false

            val hashedKey = hashKey(key)
            val secretKey = getOrCreateSecretKey(hashedKey)
            val cipher = Cipher.getInstance(transformation)

            cipher.init(Cipher.ENCRYPT_MODE, secretKey)

            val iv = cipher.iv
            val encryptedData = cipher.doFinal(`val`.toByteArray())

            val combined = iv + encryptedData
            val encodedData = Base64.encodeToString(combined, Base64.DEFAULT)

            val sharedPreferences = reactApplicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(hashedKey, encodedData).apply()

            true
        } catch (e: Exception) {
            false
        }
    }

    override fun getItem(key: String?): String? {
        return try {
            if (key.isNullOrEmpty()) return null

            val hashedKey = hashKey(key)
            val sharedPreferences = reactApplicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val encodedData = sharedPreferences.getString(hashedKey, null) ?: return null

            val combined = Base64.decode(encodedData, Base64.DEFAULT)
            if (combined.size < 12) return null // Prevent IV corruption

            val iv = combined.copyOfRange(0, 12)
            val encryptedData = combined.copyOfRange(12, combined.size)

            val secretKey = getOrCreateSecretKey(hashedKey)
            val cipher = Cipher.getInstance(transformation)

            cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))

            String(cipher.doFinal(encryptedData))
        } catch (e: BadPaddingException) {
            null
        } catch (e: IllegalBlockSizeException) {
            null
        } catch (e: Exception) {
            null
        }
    }

    override fun removeItem(key: String?): Boolean {
        return try {
            if (key.isNullOrEmpty()) return false

            val hashedKey = hashKey(key)
            val sharedPreferences = reactApplicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit().remove(hashedKey).apply()

            true
        } catch (e: Exception) {
            false
        }
    }

    override fun clear() {
        val sharedPreferences = reactApplicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    private fun getOrCreateSecretKey(alias: String): SecretKey {
        return keyCache[alias] ?: run {
            val key = if (keyStore.containsAlias(alias)) {
                (keyStore.getEntry(alias, null) as KeyStore.SecretKeyEntry).secretKey
            } else {
                val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
                keyGenerator.init(
                    KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .build()
                )
                keyGenerator.generateKey()
            }
            keyCache[alias] = key
            key
        }
    }

    private fun hashKey(key: String): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(key.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }


  companion object {
    const val NAME = "KeychainStore"
    const val PREFS_NAME = "keychain_prefs"
  }
}
