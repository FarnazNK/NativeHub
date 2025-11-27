#  NativeHub - Advanced React Native Showcase

<p align="center">
  <img src="https://img.shields.io/badge/React_Native-0.73-blue?logo=react" alt="React Native"/>
  <img src="https://img.shields.io/badge/Kotlin-1.9-purple?logo=kotlin" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/Swift-5.9-orange?logo=swift" alt="Swift"/>
  <img src="https://img.shields.io/badge/TypeScript-5.3-blue?logo=typescript" alt="TypeScript"/>
</p>

##  Overview

**NativeHub** is an open-source React Native application demonstrating senior-level mobile development expertise with custom native modules for Android (Kotlin) and iOS (Swift).

### Key Features

-  **Biometric Authentication** - Native Face ID/Touch ID & Android BiometricPrompt
-  **Performance Monitoring** - Custom native performance metrics collection
-  **Secure Storage** - Encrypted data persistence using Keychain/Keystore
-  **Adaptive UI** - Dynamic theming with dark/light mode support

##  Architecture
```
NativeHub/
├── src/
│   ├── components/          # Reusable UI components
│   ├── screens/             # Screen components
│   ├── navigation/          # Navigation configuration
│   ├── hooks/               # Custom React hooks
│   ├── services/            # Business logic & API
│   ├── store/               # Zustand state management
│   ├── native-modules/      # Native module TypeScript interfaces
│   └── theme/               # Theming & styling
├── android/
│   └── app/src/main/kotlin/com/nativehub/
│       ├── biometric/       # Android biometric module
│       └── performance/     # Android performance module
├── ios/NativeHub/
│   ├── Biometric/           # iOS biometric module
│   └── Performance/         # iOS performance module
└── __tests__/               # Test suites
```

##  Installation
```bash
# Clone the repository
git clone https://github.com/FarnazNK/NativeHub.git
cd NativeHub

# Install dependencies
npm install

# iOS setup
cd ios && pod install && cd ..

# Run
npm run ios
npm run android
```

##  Native Modules

### Biometric Authentication

**Android (Kotlin)** - Uses BiometricPrompt API  
**iOS (Swift)** - Uses LocalAuthentication framework
```typescript
import { Biometric } from './native-modules';

const result = await Biometric.authenticate({
  title: 'Authenticate',
  description: 'Verify your identity',
});
```

### Performance Monitoring
```typescript
import { Performance } from './native-modules';

const metrics = await Performance.getMetrics();
console.log('CPU:', metrics.cpuUsage);
console.log('Memory:', metrics.memoryUsage);
```

##  Testing
```bash
npm run test:unit        # Unit tests
npm run test:coverage    # With coverage
npm run test:e2e:ios     # E2E iOS
npm run test:e2e:android # E2E Android
```

---

## 👩‍💻 Author

### Farnaz Nasehi

**Expertise:**
-  React Native & Cross-Platform Development
-  Native Android Development (Kotlin/Java)
-  Native iOS Development (Swift/Objective-C)
-  Mobile Architecture & Performance Optimization
