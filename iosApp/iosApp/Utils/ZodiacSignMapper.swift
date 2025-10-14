//
//  ZodiacSignMapper.swift
//  iosApp
//
//  Created by Carla von Eicken on 14.10.25.
//

import Foundation
import SwiftUI
import Shared

func uiImageName(for sign: ZodiacSign?) -> String? {
    guard let sign = sign else { return nil }
    switch sign {
    case .capricorn: return "capricorn"
    case .aquarius:  return "aquarius"
    case .pisces:    return "pisces"
    case .aries:     return "aries"
    case .taurus:    return "taurus"
    case .gemini:    return "gemini"
    case .cancer:    return "cancer"
    case .leo:       return "leo"
    case .virgo:     return "virgo"
    case .libra:     return "libra"
    case .scorpio:   return "scorpio"
    case .sagittarius: return "sagittarius"
    default:
        return "no image found"
    }
}
