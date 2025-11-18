//
//  BirthdayDetailContent.swift
//  iosApp
//
//  Created by Carla von Eicken on 17.11.25.
//

import SwiftUI
import Shared
import KMPObservableViewModelSwiftUI


// Top layer of the BirthdayDetailScreen -> covers ProgressView, the actual DetailView (inlcuding the Edit-Birthday-Button) and NotFoundStateView
struct BirthdayDetailContent: View {
    let uiState: BirthdayDetailViewModel.UiState
    let onDelete: () -> Void
    let onRetry: () -> Void
    
    var body: some View {
        ZStack {
            Color(backgroundLight).ignoresSafeArea()
            
            if uiState.isLoading {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle(tint: Color(goldPrimary)))
                    .scaleEffect(1.5)
            } else if let birthday = uiState.birthday {
                BirthdayDetailScrollView(birthday: birthday)
                    // Overlay with Edit-Birthday-Button
                    .overlay(alignment: .bottomTrailing) {
                        NavigationLink(
                            destination: EditBirthdayScreen(birthdayId: birthday.id)
                        ) {
                            Image(systemName: "pencil")
                                .foregroundColor(.white)
                                .font(.system(size: 36))
                                .frame(width: 60, height: 60)
                                .background(Color(goldPrimary))
                                .clipShape(Circle())
                                .shadow(radius: 4)
                        }
                        .padding()
                        .padding(.trailing, 15)
                    }
            } else {
                NotFoundStateView(
                    onRetry: onRetry
                )
            }
        }
    }
}
