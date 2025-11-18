//
//  BirthdayDetailScreen.swift
//  iosApp
//
//  Created by Carla von Eicken on 31.10.25.
//

import SwiftUI
import Shared
import KMPObservableViewModelSwiftUI


struct BirthdayDetailScreen: View {
    let birthdayId: Int64
    @StateViewModel var viewModel: BirthdayDetailViewModel
    
    @Environment(\.dismiss) private var dismiss
    @EnvironmentObject private var toastCenter: ToastCenter
    
    @State var showDeleteDialog = false
    
    init(birthdayId: Int64) {
        self.birthdayId = birthdayId
        _viewModel = StateViewModel(
            wrappedValue: BirthdayDetailViewModel(birthdayId: birthdayId)
        )
    }
    
    var body: some View {
        BirthdayDetailContent(
            uiState: viewModel.uiState,
            onDelete: { viewModel.deleteBirthday() },
            onRetry: { viewModel.loadBirthday(birthdayId: birthdayId) }
        )
        .navigationBarBackButtonHidden(true)
        .toolbar {
            // Back button
            ToolbarItem(placement: .navigationBarLeading) {
                Button(action: { dismiss() }) {
                    Image(systemName: "chevron.left")
                        .foregroundColor(Color(textPrimary))
                }
            }
            
            // Delete button
            ToolbarItem(placement: .navigationBarTrailing) {
                Button(action: { showDeleteDialog = true }) {
                    Image(systemName: "trash")
                        .foregroundColor(Color(textPrimary))
                }
            }
        }
        // Delete birthday alert
        .alert("Delete birthday", isPresented: $showDeleteDialog) {
            Button("Cancel", role: .cancel) { }
            Button("Delete", role: .destructive) {
                viewModel.deleteBirthday()
                dismiss()
            }
        } message: {
            Text("Are you sure you want to delete this birthday? This action cannot be undone.")
        }

        // Check if the uiState for success- or errormessage changes and if so, display a toast
        .onChange(of: viewModel.uiState.successMessage) { _, message in
            if let message {
                viewModel.clearMessages()
                toastCenter.success(message)
                dismiss()
            }
        }
        .onChange(of: viewModel.uiState.errorMessage) { _, message in
            if let message {
                viewModel.clearMessages()
                toastCenter.error(message)
            }
        }
    }
}
