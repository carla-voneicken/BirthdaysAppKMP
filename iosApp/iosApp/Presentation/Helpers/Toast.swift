//
//  Toast.swift
//  iosApp
//
//  Created by Carla von Eicken on 03.11.25.
//

import SwiftUI

// MARK: ToastView
struct ToastView: View {
    
    var style: ToastStyle
    var message: String
    var width = CGFloat.infinity
    var onCancelTapped: (() -> Void)
    
    var body: some View {
        HStack(alignment: .center, spacing: 12) {
            Image(systemName: style.iconFileName)
                .foregroundColor(style.themeColor)
                .font(.system(size: 20))
            Text(message)
                .font(.system(size: 15))
                .foregroundColor(.primary)
                .multilineTextAlignment(.leading)
            
            Spacer(minLength: 10)
            
            Button {
                onCancelTapped()
            } label: {
                Image(systemName: "xmark")
                    .foregroundStyle(.secondary)
                    .font(.system(size: 18))
            }
        }
        .padding(.horizontal, 20)
        .padding(.vertical, 16)
        .frame(minWidth: 0, maxWidth: width)
        .background(
            RoundedRectangle(cornerRadius: 14)
                .fill(.thinMaterial)
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
            .overlay(
                ZStack {
                    mainToastView()
                }.animation(.spring(), value: toast)
            )
            // calls showToast when state of toast changes
            .onChange(of: toast) { _, value in
                showToast()
            }
    }
    
    // helper that builds the visual toast when toast != nil
    @ViewBuilder func mainToastView() -> some View {
        if let toast = toast {
//            ZStack {
//                Color.black.opacity(0.01)
//                    .ignoresSafeArea()
//                    .onTapGesture {
//                        dismissToast()
//                    }
                ToastView(
                    style: toast.style,
                    message: toast.message,
                    width: toast.width
                ) {
                    // a callback for manual dismiss (e.g. tapping th x on the toast)
                    dismissToast()
                }
//            }
        }
    }
    
    private func showToast() {
        guard let toast = toast else { return }
        
        UIImpactFeedbackGenerator(style: .light)
            .impactOccurred()
        
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
        withAnimation {
            toast = nil
        }
        
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



// MARK: Toast Struct
struct Toast: Equatable {
    var style: ToastStyle
    var message: String
    var duration: Double = 3
    var width: Double = .infinity
}

// MARK: Toast Style
enum ToastStyle {
    case error
    case warning
    case success
    case info
}

extension ToastStyle {
    var themeColor: Color {
        switch self {
        case .error: return Color.red
        case .warning: return Color.orange
        case .info: return Color.blue
        case .success: return Color.green
        }
    }
    
    var iconFileName: String {
        switch self {
        case .info: return "info.circle.fill"
        case .warning: return "exclamationmark.triangle.fill"
        case .success: return "checkmark.circle.fill"
        case .error: return "xmark.circle.fill"
        }
    }
}
