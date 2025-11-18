//
//  BirthdayItemCard.swift
//  iosApp
//
//  Created by Carla von Eicken on 16.10.25.
//

import SwiftUI
import Shared

struct BirthdayItemCard: View {
    let birthday: Birthday
    
    var body: some View {
        HStack(spacing: 16) {
            // Zodiac circle on the left
            ZStack {
                Circle()
                    .stroke(goldPrimary, lineWidth: 3)
                    .frame(width: 60, height: 60)
                
                Image(uiImageName(for: birthday.zodiacSign) ?? "BirthdayCake")
                    .resizable()
                    .frame(width: 50, height: 50)
            }.frame(width: 60, height: 60)
            
            // Name and birthday info
            VStack(alignment: .leading, spacing: 4) {
                Text(birthday.name)
                    .font(.system(size: 18, weight: .bold))
                    .foregroundColor(.black)
                    .foregroundStyle(textPrimary)
                
                Text(
                    buildDateText(for: birthday)
                )
                .font(.system(size: 12, weight: .medium))
                .foregroundStyle(orangeAccent)
                
            }
            
            Spacer()
            
            // Days countdown badge on the right
            VStack(spacing: 0) {
                let parts = birthday.daysFromNow.split(separator: " ")
                if (parts.count == 2) {
                    Text(parts[0])
                        .font(.system(size: 18, weight: .bold))
                        .foregroundColor(turquoiseSeconday)
                    Text(parts[1])
                        .font(.system(size: 12, weight: .medium))
                        .foregroundColor(turquoiseSeconday)
                } else {
                    Text(birthday.daysFromNow)
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(turquoiseSeconday)
                }
            }
        }
        .padding(16)
        .background(Color.white)
        .cornerRadius(16)
        .shadow(color: Color.black.opacity(0.1), radius: 4, x: 0, y: 2)
    }
    
    private func buildDateText(for birthday: Birthday) -> String {
        let formattedDate = BirthdayFormatterKt.formattedNextBirthday(birthday: birthday, pattern: "EEE, d. MMM yy")
        
        if let nextAge = birthday.nextAge {
            return "turning \(nextAge) on \(formattedDate)"
        } else {
            return "on \(formattedDate)"
        }
    }
}
