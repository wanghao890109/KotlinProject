package org.example.project.uitls

import platform.Foundation.NSOperationQueue
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication

actual object AToast {
    actual fun show(message: String) {
        val alert = UIAlertController.alertControllerWithTitle(
            title = null,
            message = message,
            preferredStyle = UIAlertControllerStyleAlert
        )

        // 自动消失的 Toast
        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            alert,
            animated = true,
            completion = {
                // 1.5 秒后自动消失
                NSOperationQueue.mainQueue().addOperationWithBlock {
                    alert.dismissViewControllerAnimated(true, completion = null)
                }
            }
        )
    }
}