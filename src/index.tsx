import KeychainStore from './NativeKeychainStore';

export function getItem(key: string) {
  return KeychainStore.getItem(key);
}

export function removeItem(key: string) {
  return KeychainStore.removeItem(key);
}

export function clear() {
  return KeychainStore.clear();
}

export function setItem(key: string, value: string) {
  return KeychainStore.setItem(key, value);
}

export default KeychainStore;
