//
//  ToastCenter.swift
//  iosApp
//
//  Created by Carla von Eicken on 17.11.25.
//

import SwiftUI

final class ToastCenter: ObservableObject {
    @Published var toast: Toast?

    // sets the toast from any screen, depending on whether it's a success or error message
    func success(_ message: String, duration: Double = 2) {
        toast = Toast(message: message, duration: duration, type: .success)
    }

    func error(_ message: String, duration: Double = 2) {
        toast = Toast(message: message, duration: duration, type: .error)
    }


    func clear() {
        self.toast = nil
    }
}
