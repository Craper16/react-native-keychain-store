import KeychainStore from '../NativeKeychainStore';
import { getItem, setItem, removeItem, clear } from '../index';

jest.mock('../NativeKeychainStore', () => ({
  getItem: jest.fn(),
  setItem: jest.fn(),
  removeItem: jest.fn(),
  clear: jest.fn(),
}));

const mockedKeychainStore = KeychainStore as jest.Mocked<typeof KeychainStore>;

describe('KeychainStore', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('getItem should call KeychainStore.getItem with correct key', () => {
    mockedKeychainStore.getItem.mockReturnValue('testValue');
    const value = getItem('testKey');
    expect(mockedKeychainStore.getItem).toHaveBeenCalledWith('testKey');
    expect(value).toBe('testValue');
  });

  test('setItem should call KeychainStore.setItem with correct key and value', () => {
    setItem('testKey', 'testValue');
    expect(mockedKeychainStore.setItem).toHaveBeenCalledWith(
      'testKey',
      'testValue'
    );
  });

  test('removeItem should call KeychainStore.removeItem with correct key', () => {
    removeItem('testKey');
    expect(mockedKeychainStore.removeItem).toHaveBeenCalledWith('testKey');
  });

  test('clear should call KeychainStore.clear', () => {
    clear();
    expect(mockedKeychainStore.clear).toHaveBeenCalled();
  });
});
