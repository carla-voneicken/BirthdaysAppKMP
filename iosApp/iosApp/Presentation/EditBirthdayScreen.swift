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
    
    @State private var toast: Toast? = nil
    let onShowToast: ((Toast) -> Void)?
    
    @Environment(\.dismiss) private var dismiss
    
    init(birthdayId: Int64?, onShowToast: ((Toast) -> Void)? = nil) {
        self.birthdayId = birthdayId
        self.onShowToast = onShowToast
        
        let kotlinBirthdayId: KotlinLong? = if let id = birthdayId {
            KotlinLong(value: id)
        } else {
            nil
        }
        
        _viewModel = StateViewModel(
            wrappedValue: EditBirthdayViewModel(birthdayId: kotlinBirthdayId)
        )
    }
    
    @FocusState private var focusedField: Field?
    enum Field {
        case name, day, month, year
    }
    
    var body: some View {
        
        VStack(alignment: .leading) {
            HStack {
                Image(systemName: "person")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 18, height: 18)
                    .foregroundColor(Color(orangeAccent))
                
                Text("Name")
                    .font(.system(size: 18))
                    .fontWeight(.semibold)
                    .foregroundStyle(Color(orangeAccent))
            }
            .padding(.bottom, 10)
            
            StyledTextField(
                label: "Name",
                text: Binding(
                    get: { viewModel.uiState.name },
                    set: viewModel.updateName
                ),
                focusedField: $focusedField,
                field: .name,
                prompt: "Name"
            )
            .padding(.bottom, 15)
            
            HStack {
                Image(systemName: "calendar")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 18, height: 18)
                    .foregroundColor(Color(orangeAccent))
                
                Text("Birthdate")
                    .font(.system(size: 18))
                    .fontWeight(.semibold)
                    .foregroundStyle(Color(orangeAccent))
            }
            .padding(.bottom, 25)
            
            HStack {
                StyledTextField(
                    label: "Day",
                    text: Binding(
                        get: { viewModel.uiState.day },
                        set: viewModel.updateDay
                    ),
                    focusedField: $focusedField,
                    field: .day,
                    prompt: "DD"
                )
                StyledTextField(
                    label: "Month",
                    text: Binding(
                        get: { viewModel.uiState.month },
                        set: viewModel.updateMonth
                    ),
                    focusedField: $focusedField,
                    field: .month,
                    prompt: "MM"
                )
                StyledTextField(
                    label: "Year (optional)",
                    text: Binding(
                        get: { viewModel.uiState.year },
                        set: viewModel.updateYear
                    ),
                    focusedField: $focusedField,
                    field: .year,
                    prompt: "YYYY"
                )
            }
            Spacer()
            HStack(alignment: .center, spacing: 20) {
                Button {
                    dismiss()
                } label: {
                    Text("Cancel")
                        .font(.system(size: 18))
                        .fontWeight(.semibold)
                        .foregroundStyle(Color(textPrimary))
                        .padding(10)
                        .padding(.horizontal, 30)
                }
                .buttonStyle(.bordered)
                
                Button {
                    viewModel.saveBirthday()
                } label: {
                    Text(viewModel.uiState.isNew ? "Save" : "Update")
                        .font(.system(size: 18))
                        .fontWeight(.semibold)
                        .foregroundStyle(.white)
                        .padding(10)
                        .padding(.horizontal, 30)
                }
                .buttonStyle(.borderedProminent)
                .tint(Color(goldPrimary))
            }
            .frame(maxWidth: .infinity)
        }
        .padding(25)
        .navigationTitle(birthdayId != nil ? "Edit Birthday" : "New Birthday")
        
        // Check if the uiState for success- or errormessage changes
        .onChange(of: viewModel.uiState.successMessage) { _, message in
            if let message {
                viewModel.clearMessages()
                onShowToast?(Toast(message: message, width: 260))
                dismiss()
            }
        }
        .onChange(of: viewModel.uiState.errorMessage) { _, message in
            if let message {
                viewModel.clearMessages()
                toast = Toast(message: message, width: 260)
            }
        }
        .toastView(toast: $toast)
    }
}


// MARK: Styles Text Field
struct StyledTextField<Field: Hashable>: View {
    let label: String
    @Binding var text: String
    @FocusState var isFocused: Bool
    @FocusState.Binding var focusedField: Field?
    let field: Field
    let prompt: String
    
    var body: some View {
        // The floating label
        ZStack(alignment: .leading) {
            if(label != "Name") {
                Text(label)
                    .foregroundColor(isFocused ? Color(orangeAccent) : .gray)
                    .font(.system(size: 12))
                    .offset(y: -31)
                    .padding(.horizontal, 4)
                    .background(Color.white)
            }
            TextField(label, text: $text, prompt: Text(prompt))
                .focused($isFocused)
                .padding(12)
                .background(Color.white)
        }
        .overlay(
            RoundedRectangle(cornerRadius: 8)
                .stroke(isFocused ? Color(orangeAccent) : Color.gray.opacity(0.5), lineWidth: 1)
        )
        .padding(.bottom, 20)
    }
}
