//
//  BirthdayDetailScrollView.swift
//  iosApp
//
//  Created by Carla von Eicken on 17.11.25.
//

import SwiftUI
import Shared
import KMPObservableViewModelSwiftUI


// Displays the actual detail content once it's loaded -> displays zodiac sign, name and the card with birthday and age plus the playful countdown
struct BirthdayDetailScrollView: View {
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
                    .font(.system(size: 20, weight: .semibold))
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
