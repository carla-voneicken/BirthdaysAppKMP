//
//  Toast.swift
//  iosApp
//
//  Created by Carla von Eicken on 03.11.25.
//

import SwiftUI

// MARK: Toast Struct
struct Toast: Equatable {
    var message: String
    var duration: Double = 2
    var width: Double = .infinity
}

// MARK: ToastView
struct ToastView: View {
    
    var message: String
    var width = CGFloat.infinity
    
    var body: some View {
        HStack(alignment: .center, spacing: 12) {
            Image(systemName: "checkmark.circle")
                .foregroundColor(Color(goldPrimary))
                .font(.system(size: 20))
            Text(message)
                .font(.system(size: 16))
                .foregroundColor(.primary)
                .multilineTextAlignment(.leading)
        }
        .padding(.horizontal, 20)
        .padding(.vertical, 16)
        .background(
            RoundedRectangle(cornerRadius: 14)
                .fill(.ultraThinMaterial)
                .stroke(Color(goldPrimary), lineWidth: 2)
        )
        .shadow(color: Color.black.opacity(0.1), radius: 10, x: 0, y: 4)
        .padding(.horizontal, 16)
    }
}

// MARK: ToastModifier (for displaying and dismissing the toast)
struct ToastModifier: ViewModifier {
    
    // the screen passes in a binding to its toast state -> when this becomes non-nil,
    // the modifier shows the toast, when it's set back to nil, it hides it
    @Binding var toast: Toast?
    // workItem is used to track the scheduled auto-dismiss task so it can be cancelled if needed
    @State private var workItem: DispatchWorkItem?
    
    func body(content: Content) -> some View {
        // content = whatever view the modifier is applied to
        content
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            // overlay draws the toast ON TOP of the screens content
            .overlay() {
                ZStack(alignment: .top) {
                    // This expands the overlay to the full size of `content`
                    if toast != nil {
                        Color.black.opacity(0.1)
                            .ignoresSafeArea()
                            .onTapGesture {
                                dismissToast()
                            }
                    }

                    if let toast {
                        ToastView(
                            message: toast.message,
                            width: toast.width
                        )
                        .padding(.top, 16)
//                        .allowsHitTesting(false)
                        .transition(.move(edge: .top).combined(with: .opacity))
                    }
                }
            }
            .animation(.spring(), value: toast != nil)
            // calls showToast when state of toast changes
            .onChange(of: toast) { _, _ in
                showToast()
            }
    }
    
    private func showToast() {
        guard let toast = toast else { return }
        
        // if the toast specifies a positive duration -> cancel any existing scheduled dismissal, create a new DispatchWorkItem that calls dismissToast(), schedule it to run on the main queue after toast.duration seconds
        if toast.duration > 0 {
            workItem?.cancel()
            
            let task = DispatchWorkItem {
                dismissToast()
            }
            
            workItem = task
            DispatchQueue.main.asyncAfter(deadline: .now() + toast.duration, execute: task)
        }
    }
    
    // dismisses toast by setting binding to nil, cancels any pending timer so it won#t try to dismiss again later
    private func dismissToast() {
        withAnimation { toast = nil }
        workItem?.cancel()
        workItem = nil
    }
}

// MARK: extension links view modifier and the Toast struct instance
extension View {
  func toastView(toast: Binding<Toast?>) -> some View {
    self.modifier(ToastModifier(toast: toast))
  }
}
