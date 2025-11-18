//
//  StyledTextField.swift
//  iosApp
//
//  Created by Carla von Eicken on 18.11.25.
//

import SwiftUI

// MARK: Styles Text Field
struct StyledTextField: View {
    let label: String
    @Binding var text: String
    @FocusState.Binding var focusedField: EditBirthdayField?
    let field: EditBirthdayField
    let prompt: String
    
    private var isFocused: Bool {
        focusedField == field
    }
    
    var body: some View {
      
        VStack(alignment: .leading, spacing: 4) {
            Text(label)
                .font(.caption)
                .foregroundColor(isFocused ? Color(orangeAccent) : .gray)

            TextField(label, text: $text, prompt: Text(prompt))
                .focused($focusedField, equals: field)
        }
        .padding(.horizontal, 8)
        .padding(.vertical, 6)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .stroke(isFocused ? Color(orangeAccent) : Color.gray.opacity(0.5), lineWidth: 1)
                .background(Color.white)
        )
        // make whole area tappable
        .contentShape(Rectangle())
        // focus this field when tapping label or background
        .onTapGesture {
            focusedField = field
        }
        .padding(.bottom, 20)
    }
}
