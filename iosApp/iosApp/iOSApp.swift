import SwiftUI
import Shared

@main
struct iOSApp: App {

    @StateObject private var toastCenter = ToastCenter()

    init() {
        KoinHelperKt.doInitKoinIos(useFakeData: false)
    }

    var body: some Scene {
        WindowGroup {
            BirthdaysListScreen()
            // create global object toastCenter
                .environmentObject(toastCenter)
            // $toastCenter.toast is a binding to the toastCenters' toast
                .modifier(ToastModifier(toast: $toastCenter.toast))
        }
    }
}
