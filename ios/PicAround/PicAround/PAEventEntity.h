//
//  PAEventEntity.h
//  PicAround
//
//  Created by BenA on 2/16/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface PAEventEntity : NSObject
@property (nonatomic,strong) NSString *EndDate;
@property (nonatomic,strong) NSString *StartDate;

@property (nonatomic,strong) NSString *EventId;
@property (nonatomic,strong) NSString *EventName;
@property (nonatomic,strong) NSString *LocationId;
@property (nonatomic,strong) NSString *LocationName;
@property (nonatomic,strong) NSString *ThumbLink;
@property (nonatomic,strong) UIImage *Thumbnail;

@end
