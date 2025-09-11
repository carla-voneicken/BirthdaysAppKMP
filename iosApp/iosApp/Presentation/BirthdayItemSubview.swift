//
//  BirthdayItemSubview.swift
//  BirthdaysApp
//
//  Created by Carla von Eicken on 26.06.25.
//

import SwiftUI

struct BirthdayItemSubview: View {
    let item: BirthdayItem
    @ObservedObject var viewModel: ListViewModel
    
    var body: some View {
        HStack {
            Image(item.zodiacSign.rawValue)
                .resizable()
                .frame(width: 50, height: 50)
            VStack(alignment: .leading) {
                Text(item.name)
                    .font(.title3)
                if let age = item.nextAge {
                    Text("wird \(age) am \(item.nextBirthday.weekday), \(item.nextBirthday.dayFullMonth)")
                        .font(.caption)
                } else {
                    Text("am \(item.nextBirthday.weekday), \(item.nextBirthday.dayFullMonth)")
                        .font(.caption)
                }
            }
            Spacer()
            Text(viewModel.daysFromNow(until: item.nextBirthday))
                .font(.subheadline)
                .multilineTextAlignment(.center)

        }
        .padding(5)
    }
}


#Preview {
    BirthdayItemSubview(
        item: BirthdayItem(name: "Shannon Cruz", day: 4, month: 10, year: 2021),
        viewModel: ListViewModel()
    )
}
