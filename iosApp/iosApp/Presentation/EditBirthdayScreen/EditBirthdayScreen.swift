//
//  EditBirthdayScreen.swift
//  iosApp
//
//  Created by Carla von Eicken on 31.10.25.
//

import SwiftUI
import Shared
import KMPObservableViewModelSwiftUI

struct EditBirthdayScreen: View {
    let birthdayId: Int64?
    @StateViewModel var viewModel: EditBirthdayViewModel
    
    @Environment(\.dismiss) private var dismiss
    @EnvironmentObject private var toastCenter: ToastCenter
    
    @FocusState private var focusedField: EditBirthdayField?

    init(birthdayId: Int64?) {
        self.birthdayId = birthdayId
        
        // convert optional birthday id into a KotlinLong value to pass to the viewmodel
        let kotlinBirthdayId: KotlinLong? = if let id = birthdayId {
            KotlinLong(value: id)
        } else {
            nil
        }
        // set viewmodel with the birthdayId parameter
        _viewModel = StateViewModel(
            wrappedValue: EditBirthdayViewModel(birthdayId: kotlinBirthdayId)
        )
    }
    
    var body: some View {
        EditBirthdayForm(
            uiState: viewModel.uiState,
            focusedField: $focusedField,
            onNameChange: viewModel.updateName,
            onDayChange: viewModel.updateDay,
            onMonthChange: viewModel.updateMonth,
            onYearChange: viewModel.updateYear,
            onCancel: { dismiss() },
            onSave: { viewModel.saveBirthday() }
        )
        .navigationTitle(birthdayId != nil ? "Edit Birthday" : "New Birthday")
        
        // MARK: Success -> Toast + dismiss
        .onChange(of: viewModel.uiState.successMessage) { _, message in
            if let message {
                viewModel.clearMessages()
                toastCenter.success(message)
                dismiss()
            }
        }
        // MARK: Error -> Toast only
        .onChange(of: viewModel.uiState.errorMessage) { _, message in
            if let message {
                viewModel.clearMessages()
                toastCenter.error(message)
            }
        }
    }
}
