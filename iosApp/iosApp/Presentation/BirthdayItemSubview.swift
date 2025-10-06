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
    //@ObservedViewModel var viewModel: BirthdaysViewModel
    
    var body: some View {
        HStack {
//            Image(item.zodiacSign)
//                .resizable()
//                .frame(width: 50, height: 50)
            VStack(alignment: .leading) {
                Text(item.name)
                    .font(.title3)
                if let age = item.nextAge {
                    Text("wird \(age) am \(formattedNextBirthday(item))")
                        .font(.caption)
                } else {
                    Text("am \(formattedNextBirthday(item))")
                        .font(.caption)
                }
            }
            Spacer()
            Text(Birthday.daysFromNow)
                .font(.subheadline)
                .multilineTextAlignment(.center)

        }
        .padding(5)
    }
}


#Preview {
    BirthdayItemSubview(
        item: Birthday(id: 1, name: "Shannon Cruz", day: 4, month: 10, year: 2021),
        //viewModel: BirthdaysViewModel()
    )
}
