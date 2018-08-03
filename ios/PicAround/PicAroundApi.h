//
//  PicAroundApi.h
//  PicAround
//
//  Created by BenA on 2/11/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "PAPhotoEntity.h"
typedef void (^PASearchCompletionBlock)(NSString *searchTerm, NSArray *results, NSError *error);
typedef void (^PAPhotoCompletionBlock)(UIImage *photoImage, NSError *error);

@interface PicAroundApi : NSObject


- (void)albumImagesById:(NSString *) albumId completionBlock:(PASearchCompletionBlock) completionBlock;
+ (NSString *)picAroundAlbumURLForAlbumId:(NSString *) albumId;

+ (NSString *)picAroundFullLinkURLForimageLink:(NSString *) imageLink;

@end
