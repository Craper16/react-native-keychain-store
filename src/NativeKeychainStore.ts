import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  setItem(key: string, val: string): boolean;
  getItem(key: string): string;
  removeItem(key: string): boolean;
  clear(): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('KeychainStore');
