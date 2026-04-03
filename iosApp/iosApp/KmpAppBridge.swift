import UIKit
import ComposeApp

enum KmpAppBridge {
    static func bootstrap() {
        ExportedKotlinPackages.org.cmp_arch.project.bootstrapIosApp()
    }

    static func makeRootViewController() -> UIViewController {
        ExportedKotlinPackages.org.cmp_arch.project.MainViewController()
    }
}
