import Foundation
import UIKit

@objc(PerformanceModule)
class PerformanceModule: NSObject {
    
    private var activeTraces: [String: Date] = [:]
    
    @objc static func requiresMainQueueSetup() -> Bool { return true }
    
    @objc func getMetrics(_ resolve: @escaping RCTPromiseResolveBlock,
                         rejecter reject: @escaping RCTPromiseRejectBlock) {
        let memoryInfo = getMemoryInfo()
        let batteryInfo = getBatteryInfo()
        
        resolve([
            "cpuUsage": getCPUUsage(),
            "memoryUsage": memoryInfo.0,
            "availableMemory": memoryInfo.1,
            "totalMemory": memoryInfo.2,
            "batteryLevel": batteryInfo.0,
            "isCharging": batteryInfo.1,
            "timestamp": Date().timeIntervalSince1970 * 1000
        ])
    }
    
    @objc func getFrameMetrics(_ resolve: @escaping RCTPromiseResolveBlock,
                              rejecter reject: @escaping RCTPromiseRejectBlock) {
        resolve(["fps": 60.0, "droppedFrames": 0, "totalFrames": 0, "avgFrameTime": 16.67])
    }
    
    @objc func startTrace(_ traceName: String,
                         resolver resolve: @escaping RCTPromiseResolveBlock,
                         rejecter reject: @escaping RCTPromiseRejectBlock) {
        activeTraces[traceName] = Date()
        resolve(nil)
    }
    
    @objc func stopTrace(_ traceName: String,
                        resolver resolve: @escaping RCTPromiseResolveBlock,
                        rejecter reject: @escaping RCTPromiseRejectBlock) {
        guard let startTime = activeTraces.removeValue(forKey: traceName) else {
            reject("ERROR", "Trace not found", nil)
            return
        }
        let endTime = Date()
        let duration = endTime.timeIntervalSince(startTime) * 1000
        
        resolve([
            "name": traceName,
            "startTime": startTime.timeIntervalSince1970 * 1000,
            "endTime": endTime.timeIntervalSince1970 * 1000,
            "duration": duration,
            "attributes": [:]
        ])
    }
    
    @objc func startFPSMonitoring(_ intervalMs: NSNumber?,
                                 resolver resolve: @escaping RCTPromiseResolveBlock,
                                 rejecter reject: @escaping RCTPromiseRejectBlock) {
        resolve(nil)
    }
    
    @objc func stopFPSMonitoring(_ resolve: @escaping RCTPromiseResolveBlock,
                                rejecter reject: @escaping RCTPromiseRejectBlock) {
        resolve(nil)
    }
    
    private func getMemoryInfo() -> (Double, Double, Double) {
        var info = mach_task_basic_info()
        var count = mach_msg_type_number_t(MemoryLayout<mach_task_basic_info>.size)/4
        let result = withUnsafeMutablePointer(to: &info) {
            $0.withMemoryRebound(to: integer_t.self, capacity: 1) {
                task_info(mach_task_self_, task_flavor_t(MACH_TASK_BASIC_INFO), $0, &count)
            }
        }
        let used = result == KERN_SUCCESS ? Double(info.resident_size) : 0
        let total = Double(ProcessInfo.processInfo.physicalMemory)
        return (used, total - used, total)
    }
    
    private func getBatteryInfo() -> (Double, Bool) {
        UIDevice.current.isBatteryMonitoringEnabled = true
        let level = Double(UIDevice.current.batteryLevel) * 100
        let charging = UIDevice.current.batteryState == .charging || UIDevice.current.batteryState == .full
        return (level, charging)
    }
    
    private func getCPUUsage() -> Double { return 0.0 }
}
