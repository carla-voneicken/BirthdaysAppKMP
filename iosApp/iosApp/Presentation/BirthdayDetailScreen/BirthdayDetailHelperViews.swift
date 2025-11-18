//
//  BirthdayDetailHelperViews.swift
//  iosApp
//
//  Created by Carla von Eicken on 17.11.25.
//

import SwiftUI
import Shared
import KMPObservableViewModelSwiftUI

// Small view components: zodiac sign image, birthday info card, info chips, playful countdown and NotFoundStateView
// MARK: - Zodiac Sign Image
struct ZodiacSignImage: View {
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
struct BirthdayInfoCard: View {
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
                    label: "Birthdate",
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
struct InfoChip: View {
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
                    .foregroundStyle(Color(textPrimary))
            }
            .padding(.bottom, 5)
            
            Text(value)
                .font(.system(size: 18, weight: .medium))
                .foregroundStyle(Color(textPrimary))
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.horizontal, 12)
        .padding(.vertical, 8)
        .background(Color.white.opacity(0.6))
        .cornerRadius(24)
    }
}

// MARK: - Playful Countdown
func playfulCountdown(daysFromNow: String) -> String {
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

// MARK: - Not Found State
struct NotFoundStateView: View {
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

