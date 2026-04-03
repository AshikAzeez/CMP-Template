import SwiftUI

@main
struct iOSApp: App {
    init() {
        KmpAppBridge.bootstrap()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
