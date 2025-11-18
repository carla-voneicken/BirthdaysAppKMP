//
//  BirthdayListContent.swift
//  iosApp
//
//  Created by Carla von Eicken on 17.11.25.
//

import SwiftUI
import KMPObservableViewModelSwiftUI
import Shared

struct BirthdayListContent: View {
    let uiState: BirthdaysViewModel.UiState

    var body: some View {
        ZStack(alignment: .bottomTrailing) {
            // MARK: List of Birthdays
            List(uiState.birthdays, id: \.id) { birthday in
                ZStack {
                    // "invisible" NavigationLink so it's still tappable, but doesn't show a chevron on the side
                    NavigationLink(value: birthday.id) {
                        EmptyView()
                    }
                    .opacity(0)
                    .buttonStyle(.plain)
                    // actual Card that should be displayed
                    BirthdayItemCard(birthday: birthday)
                }
                .padding([.leading, .trailing, .top], 12)
                .listRowSeparator(.hidden)
                .listRowInsets(EdgeInsets())
                .listRowBackground(Color.clear)
            }
            .listStyle(.plain)
            .navigationDestination(for: Int64.self) { birthdayId in
                BirthdayDetailScreen(
                    birthdayId: birthdayId
                )
            }
            .navigationTitle("CakeDays")
            .navigationBarTitleDisplayMode(.inline)
            
            // MARK: Floating Action Button for creating a new birthday
            NavigationLink(destination: EditBirthdayScreen(birthdayId: nil)) {
                CustomFAB()
            }
            .buttonStyle(PressScaleStyle())
            .padding(.trailing, 20)
            .padding()
        }
        .listStyle(.plain)
    }
}

