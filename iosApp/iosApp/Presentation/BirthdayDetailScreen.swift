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

    @State private var toast: Toast? = nil
    let onShowToast: ((Toast) -> Void)?
    @State var showDeleteDialog = false
    
    init(birthdayId: Int64, onShowToast: ((Toast) -> Void)? = nil) {
        self.birthdayId = birthdayId
        self.onShowToast = onShowToast
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
            ToolbarItem(placement: .navigationBarLeading) {
                Button(action: { dismiss() }) {
                    Image(systemName: "chevron.left")
                        .foregroundColor(Color(textPrimary))
                }
            }
            
            ToolbarItem(placement: .navigationBarTrailing) {
                Button(action: { showDeleteDialog = true }) {
                    Image(systemName: "trash")
                        .foregroundColor(Color(textPrimary))
                }
            }
        }
        .alert("Delete birthday", isPresented: $showDeleteDialog) {
            Button("Cancel", role: .cancel) { }
            Button("Delete", role: .destructive) {
                viewModel.deleteBirthday()
                dismiss()
            }
        } message: {
            Text("Are you sure you want to delete this birthday? This action cannot be undone.")
        }

        // Check if the uiState for success- or errormessage changes
        .onChange(of: viewModel.uiState.successMessage) { _, message in
            if let message {
                viewModel.clearMessages()
                onShowToast?(Toast(style: .success, message: message, width: 260))
                dismiss()
            }
        }
        .onChange(of: viewModel.uiState.errorMessage) { _, message in
            if let message {
                toast = Toast(style: .error, message: message, width: 260)
                viewModel.clearMessages()
            }
        }
        .toastView(toast: $toast)
    }
}

// MARK: - Content View
private struct BirthdayDetailContent: View {
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
                    .overlay(alignment: .bottomTrailing) {
                        NavigationLink(destination: EditBirthdayScreen(birthdayId: birthday.id)) {
                            Image(systemName: "pencil")
                                .foregroundColor(.white)
                                .font(.system(size: 20))
                                .frame(width: 56, height: 56)
                                .background(Color(goldPrimary))
                                .clipShape(Circle())
                                .shadow(radius: 4)
                        }
                        .padding()
                    }
            } else {
                NotFoundStateView(
                    onRetry: onRetry
                )
            }
        }
    }
}

// MARK: - Birthday Detail Scroll View
private struct BirthdayDetailScrollView: View {
    let birthday: Birthday
    
    var body: some View {
        ScrollView {
            VStack() {
                Spacer().frame(height: 30)
                
                ZodiacSignImage(zodiacSign: birthday.zodiacSign?.description_)
                    .padding(.bottom, 20)
                Text(birthday.name)
                    .font(.system(size: 30, weight: .semibold))
                    .foregroundColor(Color(textSecondary))
                    .multilineTextAlignment(.center)
                    .padding(.horizontal)
                    .padding(.bottom, 0)
                
                let zodiacSignName = birthday.zodiacSign?.description_ ?? ""
                Text(zodiacSignName.capitalized)
                    .font(.system(size: 24, weight: .semibold))
                    .foregroundColor(Color(goldPrimary))
                    .multilineTextAlignment(.center)
                    .padding(.horizontal)
                    .padding(.bottom, 20)
                
                BirthdayInfoCard(birthday: birthday)
                    .padding(.horizontal, 20)
                    .padding(.bottom, 25)

                
                Text(playfulCountdown(daysFromNow: birthday.daysFromNow))
                    .font(.system(size: 16, weight: .medium))
                    .foregroundColor(Color(orangeAccent))
                    .padding(.horizontal, 16)
                
                Spacer()
            }
        }
    }
}

// MARK: - Zodiac Sign Image
private struct ZodiacSignImage: View {
    let zodiacSign: String?
    
    var body: some View {
        ZStack {
            Circle()
                .stroke(Color(goldPrimary), lineWidth: 5)
                .frame(width: 170, height: 170)
            
            if let sign = zodiacSign {
                Image(sign)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 150, height: 150)
            }
        }
    }
}

// MARK: - Birthday Info Card
private struct BirthdayInfoCard: View {
    let birthday: Birthday
    
    var body: some View {
        VStack(spacing: 0) {
            if let nextAge = birthday.nextAge?.intValue {
                HStack(spacing: 16) {
                    InfoChip(
                        iconName: "calendar",
                        label: "Birth date",
                        value: BirthdayFormatterKt.formattedBirthDate(birthday: birthday)
                    )
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .layoutPriority(2)   // “heavier” like weight 2f
                    .lineLimit(1)

                    InfoChip(
                        iconName: "birthday.cake",
                        label: "Age",
                        value: "\(nextAge - 1)"
                    )
                    .frame(minWidth: 80, maxWidth: .infinity, alignment: .leading)
                    .layoutPriority(1)   // “lighter” like weight 1f
                    .lineLimit(1)
                }
                .frame(maxWidth: .infinity) // make the whole row fill horizontally
                .padding(20)
            } else {
                InfoChip(
                    iconName: "calendar",
                    label: "Birth date",
                    value: BirthdayFormatterKt.formattedBirthDate(birthday: birthday)
                )
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(20)
            }
        }
        .background(Color(goldPrimary).opacity(0.6))
        .cornerRadius(12)
    }
}

// MARK: - Info Chip
private struct InfoChip: View {
    let iconName: String
    let label: String
    let value: String
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack(spacing: 6) {
                Image(systemName: iconName)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 18, height: 18)
                    .foregroundColor(Color(textPrimary))
                
                Text(label)
                    .font(.system(size: 16))
                    .foregroundColor(Color(textPrimary))
            }
            .padding(.bottom, 5)
            
            Text(value)
                .font(.system(size: 18, weight: .medium))
                .foregroundColor(Color(textPrimary))
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.horizontal, 12)
        .padding(.vertical, 8)
        .background(Color.white.opacity(0.6))
        .cornerRadius(24)
    }
}

// MARK: - Not Found State
private struct NotFoundStateView: View {
    let onRetry: () -> Void
    @Environment(\.dismiss) private var dismiss
    
    var body: some View {
        VStack(spacing: 16) {
            Text("Birthday not found")
                .font(.title3)
                .foregroundColor(Color(textPrimary))
            
            HStack(spacing: 8) {
                Button(action: { dismiss() }) {
                    Text("Back")
                        .foregroundColor(.primary)
                        .padding(.horizontal, 20)
                        .padding(.vertical, 10)
                        .background(Color.white)
                        .cornerRadius(8)
                        .overlay(
                            RoundedRectangle(cornerRadius: 8)
                                .stroke(Color.gray, lineWidth: 1)
                        )
                }
                
                Button(action: onRetry) {
                    Text("Retry")
                        .foregroundColor(.primary)
                        .padding(.horizontal, 20)
                        .padding(.vertical, 10)
                        .background(Color.white)
                        .cornerRadius(8)
                        .overlay(
                            RoundedRectangle(cornerRadius: 8)
                                .stroke(Color.gray, lineWidth: 1)
                        )
                }
            }
        }
    }
}

// MARK: - Helper Functions
private func playfulCountdown(daysFromNow: String) -> String {
    let countdown = daysFromNow.lowercased()
    switch countdown {
    case "today!":
        return "🎉 It's today! Don't forget to celebrate."
    case "1 day":
        return "🎈 Tomorrow! Time to get the cake ready."
    default:
        return "⏳ Only \(daysFromNow) to go — better start planning!"
    }
}

#Preview {
    BirthdayDetailScreen(birthdayId: 1)
}
