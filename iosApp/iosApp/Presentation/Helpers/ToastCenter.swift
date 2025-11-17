//
//  ToastCenter.swift
//  iosApp
//
//  Created by Carla von Eicken on 17.11.25.
//

import SwiftUI

final class ToastCenter: ObservableObject {
    @Published var toast: Toast?

    func show(_ toast: Toast) {
        self.toast = toast
    }

    func clear() {
        self.toast = nil
    }
}
