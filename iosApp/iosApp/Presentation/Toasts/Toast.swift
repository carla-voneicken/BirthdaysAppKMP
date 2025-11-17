//
//  Toast.swift
//  iosApp
//
//  Created by Carla von Eicken on 03.11.25.
//

import SwiftUI


// MARK: Toast Struct
struct Toast: Equatable {
    var message: String
    var duration: Double = 2
    var type: ToastType
    
    init(
        message: String,
        duration: Double = 2,
        type: ToastType
    ) {
        self.message = message
        self.duration = max(0.1, duration)   // Ensures duration is always positive
        self.type = type
    }
}

// MARK: Toast Type - error or success message
enum ToastType {
    case success
    case error
}

extension ToastType {
    var iconFileName: String {
        switch self {
        case .success: return "checkmark.circle"
        case .error: return "exclamationmark.triangle"
        }
    }
    
    var tintColor: Color {
        switch self {
        case .success: return Color(goldPrimary)
        case .error:   return .red
        }
    }
}
