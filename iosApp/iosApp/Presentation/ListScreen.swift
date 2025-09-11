//
//  ListScreen.swift
//  BirthdaysApp
//
//  Created by Carla von Eicken on 25.06.25.
//

import SwiftUI

struct ListScreen: View {
    @StateObject private var viewModel = ListViewModel()
    
    var body: some View {
        List {
            // Loop through all months and display their name as the section header
            ForEach(viewModel.months, id: \.self) { month in
                Section(header: Text(month.monthName)) {
                    
                    // Loop through all birthdays grouped to that month
                    ForEach(viewModel.groupedItems[month] ?? [], id: \.id) { item in
                        BirthdayItemSubview(item: item, viewModel: viewModel)
                    }
                }
            }
        }
    }
}



#Preview {
    ListScreen()
}

