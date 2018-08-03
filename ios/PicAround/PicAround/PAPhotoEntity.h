//
//  PAPhotoEntity.h
//  PicAround
//
//  Created by BenA on 2/11/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface PAPhotoEntity : NSObject
@property (nonatomic,strong) UIImage *Thumbnail;
@property (nonatomic,strong) UIImage *LargeImage;

@property (nonatomic,strong) NSString *Link;
@property (nonatomic,strong) NSString *ThumbLink;
@property (nonatomic,strong) NSString *UserId;
@property (nonatomic,strong) NSString *EventName;
@property (nonatomic,strong) NSString *LocationName;
@property (nonatomic,strong) NSString *MidQLink;
@property (nonatomic,strong) NSString *Description;

@end
