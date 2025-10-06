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
    
    var body: some View {
        List(viewmodel.uiState.birthdays, id: \.id) { birthday in
            BirthdayItemSubview(item: birthday)
        }
        .toolbar {
            ToolbarItem(placement: .topBarTrailing) {
                //SortToolbarButton()
            }
        }
    }
}



#Preview {
    ListScreen()
}

