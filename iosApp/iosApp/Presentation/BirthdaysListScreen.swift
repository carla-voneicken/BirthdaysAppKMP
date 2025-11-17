//
//  ListScreen.swift
//  BirthdaysApp
//
//  Created by Carla von Eicken on 25.06.25.
//

import SwiftUI
import KMPObservableViewModelSwiftUI
import Shared

struct BirthdaysListScreen: View {
    @StateViewModel var viewmodel = BirthdaysViewModel()
    @State private var showSortMenu = false
    @State private var listToast: Toast?
    
    var body: some View {
        NavigationStack {
            ZStack(alignment: .bottomTrailing) {
                // MARK: List of Birthdays
                List(viewmodel.uiState.birthdays, id: \.id) { birthday in
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
                        birthdayId: birthdayId,
                        onShowToast: { toastMessage in
                            listToast = toastMessage
                        }
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
            .background(Color(.systemGray6))
            .toolbar {
                // MARK: Settings Button
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(action: {
                        print("Settings tapped")
                    }) {
                        Image(systemName: "gearshape")
                            .font(.system(size: 16))
                            .imageScale(.medium)
                            .foregroundColor(.primary)
                    }
                }
                // MARK: Sort Menu
                ToolbarItem(placement: .navigationBarTrailing) {
                    HStack(spacing: 12) {
                        Menu {
                            Button(action: {
                                viewmodel.setSortMode(mode: .byUpcoming)
                            }) {
                                Label("Sort by Date", systemImage: "calendar")
                            }
                            
                            Button(action: {
                                viewmodel.setSortMode(mode: .byName)
                            }) {
                                Label("Sort by Name", systemImage: "textformat")
                            }
                        } label: {
                            Image(systemName: "arrow.up.arrow.down")
                                .font(.system(size: 16))
                                .imageScale(.medium)
                                .foregroundColor(.primary)
                        }
                    }
                }
            }
            // display a toast if an item was successfully deleted in the Detail view
            .toastView(toast: $listToast)
        }
    }
}
