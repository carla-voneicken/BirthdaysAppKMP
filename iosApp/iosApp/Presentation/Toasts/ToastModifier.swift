//
//  ToastModifier.swift
//  iosApp
//
//  Created by Carla von Eicken on 17.11.25.
//


import SwiftUI

// MARK: ToastModifier (for displaying and dismissing the toast)
struct ToastModifier: ViewModifier {
    
    // the ToastCenter passes in a binding like $toastCenter.toast
    // -> when this becomes non-nil, the modifier shows the toast, when it's set back to nil, it hides it
    @Binding var toast: Toast?
    
    // workItem is used to track the scheduled auto-dismiss task so it can be cancelled if needed
    @State private var workItem: DispatchWorkItem?
    
    func body(content: Content) -> some View {
        // content = whatever view the modifier is applied to
        content
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            // overlay draws the toast ON TOP of the screens content
            .overlay() {
                // aligns toast to top of the screen
                ZStack(alignment: .top) {
                    // full-screen, almost invisible background
                    if toast != nil {
                        Color.black.opacity(0.1)
                            .ignoresSafeArea()
                        // dismiss toast if user tapps the background
                            .onTapGesture { dismissToast() }
                    }
                    // the actual toast card
                    if let toast {
                        ToastView(
                            message: toast.message,
                            type: toast.type
                        )
                        .padding(.top, 16)
                        // animated in/out with a slide-from-top + fade
                        .transition(.move(edge: .top).combined(with: .opacity))
                    }
                }
            }
            // calls showToast when state of toast changes
            .onChange(of: toast) { oldValue, newValue in
                // don't call showToast() when the toast changes back to nil
                guard newValue != nil else { return }
                showToast()
            }
    }
    
    // when a toast appears, we want to show now, automatically disappear after duration seconds,
    // BUT if another toast appears before that, the old timer should be cancelled and if the user taps outside, it should dismiss immediately and cancel the timer too
    private func showToast() {
        // cancel any previous scheduled dismissal
        workItem?.cancel()
        
        // read duration from the current toast (if present), fallback to 2 seconds
        let duration = toast?.duration ?? 2
        
        // DispatchWorkItem = a block of code that we can schedule to run later and cancel if necessary
        // create a cancelable job that will call dismissToast() when it runs -> does NOT run yet
        let task = DispatchWorkItem {
            dismissToast()
        }
        // remember the task so we can cancel it later (see property at the top of the modifier)
        workItem = task
        
        // DispatchQueue.main = main UI thread
        // schedule the task to run in the future -> after 'duration' seconds
        DispatchQueue.main.asyncAfter(deadline: .now() + duration, execute: task)
    }
    
    // dismisses toast by setting binding to nil, cancels any pending timer so it won't try to dismiss again later
    private func dismissToast() {
        // .cancel() -> stop the pending auto-dismiss
        workItem?.cancel()
        // -> forget the old task so future calls don’t interact with it
        workItem = nil
        
        // dismiss the toast with an animation
        withAnimation(.spring) { toast = nil }
    }
}


/*
 *** Bonus explanation: ***
 
 TRANSITIONS: how the view APPEARS and DISAPPEARS
 
 .transition(.move(edge: .top).combined(with: .opacity))

 When the toast appears, SwiftUI:
 - starts with the toast off-screen above,
 - animates it down,
 - also fades it from 0% → 100% opacity.

 When the toast disappears, SwiftUI:
 - moves it upward off-screen,
 - fades it out.

 Transitions ONLY work when a view is inserted/removed conditionally
 -> (if toast != nil { ToastView(...) })
 
 
 ANIMATIONS: animate changes to the view's properties
 
 .animation(.spring(), value: toast != nil)

 Does NOT decide how the toast enters/exits, but when the transition should animate.
 
 Typical changes it animates:
 - view appearing/disappearing (paired with transition)
 - frame changes
 - opacity
 - overlay changes
 
 Transition = WHAT animation looks like
 (“slide from top + fade”)

 Animation = WHEN animation should run
 (“animate when toast becomes nil or not nil”)
 */
