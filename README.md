# rn-keychain-store

A secure, simple, and efficient React Native module for storing sensitive data using iOS Keychain and Android Keystore. This module provides a unified API for securely storing and retrieving sensitive information across both platforms.

## Features

- 🔒 Secure storage using iOS Keychain and Android Keystore
- 🔄 Unified API for both platforms
- ⚡️ Built with React Native Turbo Modules for better performance
- 🛡️ Data encryption on Android using AES/GCM
- 🔑 Simple and intuitive API
- 📦 Zero dependencies
- 🧪 Fully tested
- 📱 Supports both iOS and Android

## Installation

```sh
# Using npm
npm install rn-keychain-store

# Using yarn
yarn add rn-keychain-store

# Using pnpm
pnpm add rn-keychain-store
```

### iOS

1. Install pods:
```sh
cd ios && pod install && cd ..
```

No additional steps required. The module will be automatically linked.

### Android

No additional steps required. The module will be automatically linked.

## Usage

```js
import KeychainStore, { setItem, getItem, removeItem, clear } from 'rn-keychain-store';

// Store sensitive data
const storeToken = async () => {
  try {
    const success = await setItem('auth_token', 'your-secure-token');
    if (success) {
      console.log('Token stored successfully');
    }
  } catch (error) {
    console.error('Failed to store token:', error);
  }
};

// Retrieve stored data
const getToken = async () => {
  try {
    const token = await getItem('auth_token');
    if (token) {
      console.log('Retrieved token:', token);
    }
  } catch (error) {
    console.error('Failed to retrieve token:', error);
  }
};

// Remove specific item
const removeToken = async () => {
  try {
    const success = await removeItem('auth_token');
    if (success) {
      console.log('Token removed successfully');
    }
  } catch (error) {
    console.error('Failed to remove token:', error);
  }
};

// Clear all stored data
const clearAll = async () => {
  try {
    await clear();
    console.log('All data cleared successfully');
  } catch (error) {
    console.error('Failed to clear data:', error);
  }
};
```

## API Reference

### `setItem(key: string, value: string): Promise<boolean>`

Stores a value securely in the keychain/keystore.

- `key`: The key to store the value under
- `value`: The value to store
- Returns: `Promise<boolean>` - `true` if successful, `false` otherwise

### `getItem(key: string): Promise<string | null>`

Retrieves a value from the keychain/keystore.

- `key`: The key to retrieve
- Returns: `Promise<string | null>` - The stored value or `null` if not found

### `removeItem(key: string): Promise<boolean>`

Removes a value from the keychain/keystore.

- `key`: The key to remove
- Returns: `Promise<boolean>` - `true` if successful, `false` otherwise

### `clear(): Promise<void>`

Removes all values from the keychain/keystore.

## Security Considerations

- On iOS, data is stored in the Keychain and persists across app uninstalls
- On Android, data is encrypted using AES/GCM and stored in the Keystore
- All operations are performed on a background thread to prevent UI blocking
- Keys are hashed before storage on Android for additional security

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
