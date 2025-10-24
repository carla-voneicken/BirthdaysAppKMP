import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        KoinHelperKt.doInitKoinIos(useFakeData: true)
        let launchSB = Bundle.main.object(forInfoDictionaryKey: "UILaunchStoryboardName") as? String
        let launchDict = Bundle.main.object(forInfoDictionaryKey: "UILaunchScreen") as? [String: Any]
        print("UILaunchStoryboardName =", launchSB ?? "nil")
        print("UILaunchScreen keys =", Array((launchDict ?? [:]).keys))
        let dict = Bundle.main.infoDictionary ?? [:]
        print("UILaunchScreen =", dict["UILaunchScreen"] ?? "nil")
    }

    var body: some Scene {
        WindowGroup {
            ListScreen()
        }
    }
}
