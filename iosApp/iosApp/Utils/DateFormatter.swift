//
//  DateFormatter.swift
//  iosApp
//
//  Created by Carla von Eicken on 06.10.25.
//

import Shared

extension DateFormatter {
    
    static let birthday: DateFormatter = {
        
        let f = DateFormatter()
        f.locale = Locale.current
        f.timeZone = .current
        f.dateFormat = "EEEE, d. MMMM yyyy"
        return f
    }()
    
}

func formattedNextBirthday(_ birthday: Birthday) -> String {
    let iso = birthday.nextBirthdayIso
    
    let isoParser = ISO8601DateFormatter()
    isoParser.timeZone = .current
    isoParser.formatOptions = [.withFullDate] // parse date-only ISO
    
    if let date = isoParser.date(from: iso) {
        return DateFormatter.birthday.string(from: date)
    } else {
        return iso // fallback
    }
}
