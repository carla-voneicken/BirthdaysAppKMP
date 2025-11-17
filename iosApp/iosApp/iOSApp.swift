import SwiftUI
import Shared

@main
struct iOSApp: App {

    @StateObject private var toastCenter = ToastCenter()

    init() {
        KoinHelperKt.doInitKoinIos(useFakeData: true)
    }

    var body: some Scene {
        WindowGroup {
            BirthdaysListScreen()
                .environmentObject(toastCenter)          // inject globally
                .toastView(toast: $toastCenter.toast)    // single global toast
        }
    }
}
