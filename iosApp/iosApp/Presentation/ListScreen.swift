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
    
    var body: some View {
        NavigationStack {
            ZStack(alignment: .bottomTrailing) {
                List(viewmodel.uiState.birthdays, id: \.id) { birthday in
                    BirthdayItemCard(birthday: birthday)
                        .listRowSeparator(.hidden)
                        .listRowInsets(EdgeInsets())
                        .listRowBackground(Color.clear)
                        .padding([.leading, .trailing, .top], 12)
                }
                .navigationTitle("CakeDays")
                .navigationBarTitleDisplayMode(.inline)
                
                Button {
                    // Action
                } label: {
                    ZStack {
                        RoundedRectangle(cornerRadius: 10)
                            .fill(goldPrimary)
                            .frame(width: 70, height: 70)
                            .shadow(color: backgroundDark.opacity(0.4), radius: 8, x: 5, y: 5)
                        Image("BirthdayCakeShadow")
                            .renderingMode(.template)
                            .resizable()
                            .scaledToFit()
                            .frame(width: 50, height: 50)
                            .foregroundStyle(.white)
                        Image(systemName: "plus")
                            .font(.system(size: 20, weight: .bold))
                            .foregroundStyle(goldPrimary)
                            .offset(y: 10)
                    }
                    .contentShape(RoundedRectangle(cornerRadius: 10)) // precise hit target
                }
                .buttonStyle(PressScaleStyle())
                .padding(.trailing, 20)
                .padding()
            }
            .listStyle(.plain)
            .background(Color(.systemGray6))
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
        }
    }
}



#Preview {
    ListScreen()
}

