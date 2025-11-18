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
    @StateViewModel var viewModel = BirthdaysViewModel()
    @State private var showSortMenu = false
    @EnvironmentObject private var toastCenter: ToastCenter
    
    var body: some View {
        NavigationStack {
            BirthdayListContent(uiState: viewModel.uiState)
            .background(Color(.systemGray6))
            // MARK: Toolbar
            .toolbar {
                // Settings Button
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
                
                // Sort Menu
                ToolbarItem(placement: .navigationBarTrailing) {
                    HStack(spacing: 12) {
                        Menu {
                            Button(action: {
                                viewModel.setSortMode(mode: .byUpcoming)
                            }) {
                                Label("Sort by Date", systemImage: "calendar")
                            }
                            
                            Button(action: {
                                viewModel.setSortMode(mode: .byName)
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
