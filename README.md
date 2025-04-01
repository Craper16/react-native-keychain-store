# rn-keychain-store

Keychain and Keystore simple API Implementation

## Installation

```sh
npm install rn-keychain-store
```

## Usage

```js
import KeychainStore, { setItem, getItem, removeItem, clear } from 'rn-keychain-store';

// ...

setItem('token', 'some-access-token-securely-stored')

const token = getItem('token')

removeItem('token')

clear()
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
