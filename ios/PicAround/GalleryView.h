//
//  GalleryView.h
//  PicAround
//
//  Created by BenA on 2/10/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PicAroundApi.h"

@interface GalleryView : UICollectionViewController

@property (nonatomic, strong) PicAroundApi *paApi;
@property (nonatomic, strong) NSArray *searchResults;
@property (nonatomic, strong) NSMutableArray *searches;

@end
