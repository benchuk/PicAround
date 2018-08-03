//
//  PicAroundApi.m
//  PicAround
//
//  Created by BenA on 2/11/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import "PicAroundApi.h"
#import "PAPhotoEntity.h"

@implementation PicAroundApi

+ (NSString *)picAroundAlbumURLForAlbumId:(NSString *) albumId
{
    albumId = [albumId stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    return [NSString stringWithFormat:@"http://picaround.azurewebsites.net/Home/GalleryByEventId?id=%@",albumId];
}

+ (NSString *)picAroundFullLinkURLForimageLink:(NSString *) imageLink
{
    imageLink = [imageLink stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    return [NSString stringWithFormat:@"http://picaround.blob.core.windows.net/%@",imageLink];
}

- (void)albumImagesById:(NSString *) albumId completionBlock:(PASearchCompletionBlock) completionBlock
{
    NSString *searchURL = [PicAroundApi picAroundAlbumURLForAlbumId:albumId];
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    
    dispatch_async(queue, ^{
        NSError *error = nil;
        NSString *queryUrl = [NSString stringWithContentsOfURL:[NSURL URLWithString:searchURL]
                                                                encoding:NSUTF8StringEncoding
                                                                   error:&error];
        if (error != nil) {
            completionBlock(albumId,nil,error);
        }
        else
        {
            // Parse the JSON Response
            NSData *jsonData = [queryUrl dataUsingEncoding:NSUTF8StringEncoding];
            NSArray *searchResultsArray = [NSJSONSerialization JSONObjectWithData:jsonData
                                                                              options:kNilOptions
                                                                                error:&error];
            if(error != nil)
            {
                completionBlock(albumId,nil,error);
            }
            else
            {
 
                NSArray *objPhotos = searchResultsArray;
                NSMutableArray *albumPhotos = [@[] mutableCopy];
                for(NSDictionary *objPhoto in objPhotos)
                {
                    PAPhotoEntity *photo = [[PAPhotoEntity alloc] init];
                    
                    photo.Link = [objPhoto objectForKey:@"Link"];
                    photo.ThumbLink = [objPhoto objectForKey:@"ThumbLink"];
                    photo.UserId = [objPhoto objectForKey:@"UserId"];
                    photo.EventName = [objPhoto objectForKey:@"EventName"];
                    photo.LocationName = [objPhoto objectForKey:@"LocationName"];
                    photo.MidQLink = [objPhoto objectForKey:@"MidQLink"];
                    photo.Description = [objPhoto objectForKey:@"Description"];
                    
                    NSString *linkToThumbImage = [PicAroundApi picAroundFullLinkURLForimageLink:photo.ThumbLink];
                    NSData *imageData = [NSData dataWithContentsOfURL:[NSURL URLWithString:linkToThumbImage]
                                                              options:0
                                                                error:&error];
                    UIImage *image = [UIImage imageWithData:imageData];
                    photo.thumbnail = image;
                    
                    NSString *linkToMidQImage = [PicAroundApi picAroundFullLinkURLForimageLink:photo.MidQLink];
                    NSData *imageDataMidQ = [NSData dataWithContentsOfURL:[NSURL URLWithString:linkToMidQImage]
                                                              options:0
                                                                error:&error];
                    UIImage *imageMidQ = [UIImage imageWithData:imageDataMidQ];
                    photo.LargeImage = imageMidQ;
                    
                    [albumPhotos addObject:photo];
                }
                
                completionBlock(albumId,albumPhotos,nil);
            }
        }
    });
}

//+ (void)loadImageForPhoto:(FlickrPhoto *)flickrPhoto thumbnail:(BOOL)thumbnail completionBlock:(FlickrPhotoCompletionBlock) completionBlock
//{
//    
//    NSString *size = thumbnail ? @"m" : @"b";
//    
//    NSString *searchURL = [Flickr flickrPhotoURLForFlickrPhoto:flickrPhoto size:size];
//    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
//    
//    dispatch_async(queue, ^{
//        NSError *error = nil;
//        
//        NSData *imageData = [NSData dataWithContentsOfURL:[NSURL URLWithString:searchURL]
//                                                  options:0
//                                                    error:&error];
//        if(error)
//        {
//            completionBlock(nil,error);
//        }
//        else
//        {
//            UIImage *image = [UIImage imageWithData:imageData];
//            if([size isEqualToString:@"m"])
//            {
//                flickrPhoto.thumbnail = image;
//            }
//            else
//            {
//                flickrPhoto.largeImage = image;
//            }
//            completionBlock(image,nil);
//        }
//        
//    });
//}


@end
