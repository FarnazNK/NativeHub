import Foundation
import LocalAuthentication

@objc(BiometricModule)
class BiometricModule: NSObject {
    
    @objc static func requiresMainQueueSetup() -> Bool { return false }
    
    @objc func isAvailable(_ resolve: @escaping RCTPromiseResolveBlock,
                          rejecter reject: @escaping RCTPromiseRejectBlock) {
        let context = LAContext()
        var error: NSError?
        let canEvaluate = context.canEvaluatePolicy(.deviceOwnerAuthenticationWithBiometrics, error: &error)
        
        resolve([
            "available": canEvaluate,
            "biometricType": getBiometricTypeString(context.biometryType)
        ])
    }
    
    @objc func getBiometricType(_ resolve: @escaping RCTPromiseResolveBlock,
                               rejecter reject: @escaping RCTPromiseRejectBlock) {
        let context = LAContext()
        _ = context.canEvaluatePolicy(.deviceOwnerAuthenticationWithBiometrics, error: nil)
        resolve(getBiometricTypeString(context.biometryType))
    }
    
    @objc func authenticate(_ options: NSDictionary?,
                           resolver resolve: @escaping RCTPromiseResolveBlock,
                           rejecter reject: @escaping RCTPromiseRejectBlock) {
        let context = LAContext()
        let reason = options?["description"] as? String ?? "Verify your identity"
        
        context.evaluatePolicy(.deviceOwnerAuthenticationWithBiometrics, localizedReason: reason) { success, error in
            DispatchQueue.main.async {
                if success {
                    resolve(["success": true])
                } else {
                    resolve([
                        "success": false,
                        "error": ["code": "AUTH_ERROR", "message": error?.localizedDescription ?? "Unknown error"]
                    ])
                }
            }
        }
    }
    
    @objc func hasDeviceCredentials(_ resolve: @escaping RCTPromiseResolveBlock,
                                   rejecter reject: @escaping RCTPromiseRejectBlock) {
        let context = LAContext()
        resolve(context.canEvaluatePolicy(.deviceOwnerAuthentication, error: nil))
    }
    
    private func getBiometricTypeString(_ type: LABiometryType) -> String {
        switch type {
        case .faceID: return "face"
        case .touchID: return "fingerprint"
        default: return "none"
        }
    }
}
