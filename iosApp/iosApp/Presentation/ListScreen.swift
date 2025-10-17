//
//  ListScreen.swift
//  BirthdaysApp
//
//  Created by Carla von Eicken on 25.06.25.
//

import SwiftUI
import KMPObservableViewModelSwiftUI
import Shared

struct ListScreen: View {
    @StateViewModel var viewmodel = BirthdaysViewModel()
    @State private var showSortMenu = false
    @State private var showAddBirthday = false
    
    var body: some View {
        NavigationStack {
            List(viewmodel.uiState.birthdays, id: \.id) { birthday in
                BirthdayItemCard(birthday: birthday)
                    .listRowSeparator(.hidden)
                    .listRowInsets(EdgeInsets())
                    .listRowBackground(Color.clear)
                    .padding([.leading, .trailing, .top], 12)
            }
            .listStyle(.plain)
            .background(Color(.systemGray6))
            .navigationTitle("Birthdays")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
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
                ToolbarItem(placement: .navigationBarTrailing) {
                    HStack(spacing: 12) {
                        Button(action: {
                            showAddBirthday = true
                        }) {
                            Image(systemName: "plus")
                                .fontWeight(.semibold)
                        }
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
            .sheet(isPresented: $showAddBirthday) {
                // AddBirthdayView()
                Text("Add Birthday Form")
            }
        }
    }
}



#Preview {
    ListScreen()
}

