//
//  BirthdayItemSubview.swift
//  BirthdaysApp
//
//  Created by Carla von Eicken on 26.06.25.
//

import SwiftUI
import Shared
import KMPObservableViewModelSwiftUI

struct BirthdayItemSubview: View {
    let item: Birthday
    
    var body: some View {
        HStack {
            if let name = uiImageName(for: item.zodiacSign),
                           let uiImage = UIImage(named: name) {
                            Image(uiImage: uiImage)
                                .resizable()
                                .frame(width: 50, height: 50)
                        }
            VStack(alignment: .leading) {
                Text(item.name)
                    .font(.headline)
                if let age = item.nextAge {
                    Text("turns \(age) on \(formattedNextBirthday(item))")
                        .font(.caption)
                } else {
                    Text("on \(formattedNextBirthday(item))")
                        .font(.caption)
                }
            }
            Spacer()
            Text(item.daysFromNow)
                .font(.subheadline)
                .multilineTextAlignment(.center)

        }
        .padding(5)
    }
}
