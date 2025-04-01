import KeychainStore from './NativeKeychainStore';

export function multiply(a: number, b: number): number {
  return KeychainStore.multiply(a, b);
}
