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
typedef void (^PAGenericBlock)(NSArray *results, NSError *error);
typedef void (^ImageLoadedBlock)(NSString *imageIndex, UIImage *photoImage);
typedef void (^CreateNewEventCompletedBlock)(NSString *res);

@interface PicAroundApi : NSObject


- (void)albumImagesById:(NSString *) albumId completionBlock:(PASearchCompletionBlock) completionBlock imageLoadedEvent:(ImageLoadedBlock) imageLoaded;

+ (NSString *)picAroundAlbumURLForAlbumId:(NSString *) albumId;

+ (NSString *)picAroundFullLinkURLForimageLink:(NSString *) imageLink;



-(void) findEventByLocation:(NSString *) userID lat: (NSString *) lat lon: (NSString *) lon radius: (NSString *) radius completionBlock:(PAGenericBlock) completionBlock imageLoadedEvent:(ImageLoadedBlock) imageLoaded;

-(void) loadUserEvents:(NSString *) userID completionBlock:(PAGenericBlock) completionBlock imageLoadedEvent:(ImageLoadedBlock) imageLoaded;

-(void) createNewEvent:(NSString *) userID withEventName:(NSString *) eventName withEventStartTime: (NSString*) eventStartDateTime withEventEndTime:(NSString*) eventEndDateTime withPlaceLat:(NSString*) placeLat withPlaceLong:(NSString*) placeLong withLocationId: (NSString*) locationID completionBlock:(CreateNewEventCompletedBlock) createNewEventCompleted;
@end
