import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        KoinHelperKt.doInitKoinIos(useFakeData: true)
    }

    var body: some Scene {
        WindowGroup {
            ListScreen()
        }
    }
}
