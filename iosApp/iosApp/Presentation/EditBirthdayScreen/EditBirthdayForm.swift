//
//  EditBirthdayForm.swift
//  iosApp
//
//  Created by Carla von Eicken on 18.11.25.
//

import SwiftUI
import Shared
import KMPObservableViewModelSwiftUI

struct EditBirthdayForm: View {
    
    let uiState: EditBirthdayViewModel.UiState
    @FocusState.Binding var focusedField: EditBirthdayField?
    
    let onNameChange: (String) -> Void
    let onDayChange: (String) -> Void
    let onMonthChange: (String) -> Void
    let onYearChange: (String) -> Void
    let onCancel: () -> Void
    let onSave: () -> Void
    
    var body: some View {
        VStack(alignment: .leading) {
            // MARK: Name Form
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
                    get: { uiState.name },
                    set: onNameChange
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
            .padding(.bottom, 10)
            
            // MARK: Birthdate Form
            HStack {
                StyledTextField(
                    label: "Day",
                    text: Binding(
                        get: { uiState.day },
                        set: onDayChange
                    ),
                    focusedField: $focusedField,
                    field: .day,
                    prompt: "DD"
                )
                StyledTextField(
                    label: "Month",
                    text: Binding(
                        get: { uiState.month },
                        set: onMonthChange
                    ),
                    focusedField: $focusedField,
                    field: .month,
                    prompt: "MM"
                )
                StyledTextField(
                    label: "Year (optional)",
                    text: Binding(
                        get: { uiState.year },
                        set: onYearChange
                    ),
                    focusedField: $focusedField,
                    field: .year,
                    prompt: "YYYY"
                )
            }
            
            Spacer()
            
            // MARK: Save and Cancel Buttons
            HStack(alignment: .center, spacing: 20) {
                Button(action: onCancel) {
                    Text("Cancel")
                        .font(.system(size: 18))
                        .fontWeight(.semibold)
                        .foregroundStyle(Color(textPrimary))
                        .padding(10)
                        .padding(.horizontal, 30)
                }
                .buttonStyle(.bordered)
                
                Button(action: onSave) {
                    Text(uiState.isNew ? "Save" : "Update")
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
    }
}


