//
//  PicAroundApi.m
//  PicAround
//
//  Created by BenA on 2/11/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import "PicAroundApi.h"
#import "PAPhotoEntity.h"
#import "PAEventEntity.h"

@implementation PicAroundApi


+ (NSString *)picAroundAlbumURLForAlbumId:(NSString *) albumId
{
    albumId = [albumId stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    return [NSString stringWithFormat:@"http://picaround.azurewebsites.net/Home/GalleryByEventId?id=%@",albumId];
}

+ (NSString *)picAroundFullLinkURLForimageLink:(NSString *) imageLink
{
    if(imageLink == [NSNull null])
    {
        return @"";
    }
    imageLink = [imageLink stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    return [NSString stringWithFormat:@"http://picaround.blob.core.windows.net/%@",imageLink];
}

+ (NSString *)findEventByLocationURL:(NSString *) userID :(NSString*) lat :(NSString*) lon :(NSString*) onlyInLocation :(NSString*) radius

{
    return [NSString stringWithFormat:@"http://picaround.azurewebsites.net/Events/FindEventByLocation?userID=%@&lat=%@&lon=%@&onlyInLocation=%@&radius=%@",userID,lat,lon,onlyInLocation,radius];
}

+ (NSString *)userEventsURL:(NSString *) userID
{
    return [NSString stringWithFormat:@"http://picaround.azurewebsites.net/User/UserEvents?userID=%@",userID];
}

-(void) findEventByLocation:(NSString *) userID lat: (NSString *) lat lon: (NSString *) lon radius: (NSString *) radius completionBlock:(PAGenericBlock) completionBlock imageLoadedEvent:(ImageLoadedBlock) imageLoaded
{
    NSString *searchURL = [PicAroundApi findEventByLocationURL:userID :lat :lon :@"true" :radius];
    
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    
    dispatch_async(queue, ^{
        NSError *error = nil;
        NSString *queryUrl = [NSString stringWithContentsOfURL:[NSURL URLWithString:searchURL]
                                                      encoding:NSUTF8StringEncoding
                                                         error:&error];
        if (error != nil) {
            completionBlock(nil,error);
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
                completionBlock(nil,error);
            }
            else
            {
                
                NSArray *objEvents = searchResultsArray;
                NSMutableArray *resEvents = [@[] mutableCopy];
                int  counter = 0;
                for(NSDictionary *objEvent in objEvents)
                {
                    NSString * index = [NSString stringWithFormat:@"%i",counter];
                    PAEventEntity *event = [[PAEventEntity alloc] init];
                    
                    event.EventName = [objEvent objectForKey:@"EventName"];
                    event.EventId = [objEvent objectForKey:@"EventId"];
                    event.EndDate = [objEvent objectForKey:@"EndDate"];
                    event.StartDate = [objEvent objectForKey:@"StartDate"];
                    event.LocationId = [objEvent objectForKey:@"LocationId"];
                    event.LocationName = [objEvent objectForKey:@"LocationName"];
                    event.ThumbLink = [objEvent objectForKey:@"ThumbLink"];
                    
                dispatch_async(queue, ^{
                    NSError *error1 = nil;
                    NSString *linkToThumbImage = [PicAroundApi picAroundFullLinkURLForimageLink:event.ThumbLink];
                    NSData *imageData = [NSData dataWithContentsOfURL:[NSURL URLWithString:linkToThumbImage]
                                                              options:0
                                                                error:&error1];
                    UIImage *image = [UIImage imageWithData:imageData];
                    event.Thumbnail = image;
                    imageLoaded(index,image);
                });
                   counter++; 
                    
                    [resEvents addObject:event];
                }
                
                completionBlock(resEvents,nil);
            }
        }
    });
    
}


-(void) loadUserEvents:(NSString *) userID completionBlock:(PAGenericBlock) completionBlock imageLoadedEvent:(ImageLoadedBlock) imageLoaded
{
    NSString *searchURL = [PicAroundApi userEventsURL:userID];
    
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    
    dispatch_async(queue, ^{
        NSError *error = nil;
        NSString *queryUrl = [NSString stringWithContentsOfURL:[NSURL URLWithString:searchURL]
                                                      encoding:NSUTF8StringEncoding
                                                         error:&error];
        if (error != nil) {
            completionBlock(nil,error);
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
                completionBlock(nil,error);
            }
            else
            {
                
                NSArray *objEvents = searchResultsArray;
                NSMutableArray *resEvents = [@[] mutableCopy];
                int  counter = 0;
                for(NSDictionary *objEvent in objEvents)
                {
                    NSString * index = [NSString stringWithFormat:@"%i",counter];
                    PAEventEntity *event = [[PAEventEntity alloc] init];
                    
                    event.EventName = [objEvent objectForKey:@"EventName"];
                    event.EventId = [objEvent objectForKey:@"EventId"];
                    event.EndDate = [objEvent objectForKey:@"EndDate"];
                    event.StartDate = [objEvent objectForKey:@"StartDate"];
                    event.LocationId = [objEvent objectForKey:@"LocationId"];
                    event.LocationName = [objEvent objectForKey:@"LocationName"];
                    event.ThumbLink = [objEvent objectForKey:@"ThumbLink"];
                    
                    dispatch_async(queue, ^{
                        NSError *error1 = nil;
                        NSString *linkToThumbImage = [PicAroundApi picAroundFullLinkURLForimageLink:event.ThumbLink];
                        NSData *imageData = [NSData dataWithContentsOfURL:[NSURL URLWithString:linkToThumbImage]
                                                                  options:0
                                                                    error:&error1];
                        UIImage *image = [UIImage imageWithData:imageData];
                        event.Thumbnail = image;
                        imageLoaded(index,image);
                    });
                    counter++;
                    
                    [resEvents addObject:event];
                }
                
                completionBlock(resEvents,nil);
            }
        }
    });
    
}


- (void)albumImagesById:(NSString *) albumId completionBlock:(PASearchCompletionBlock) completionBlock imageLoadedEvent:(ImageLoadedBlock) imageLoaded
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
                int  counter = 0;
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
                    
                    dispatch_async(queue, ^{
                        NSString * index = [NSString stringWithFormat:@"%i",counter+1];
                        NSError *error1 = nil;
                        NSString *linkToThumbImage = [PicAroundApi picAroundFullLinkURLForimageLink:photo.ThumbLink];
                        NSData *imageData = [NSData dataWithContentsOfURL:[NSURL URLWithString:linkToThumbImage]
                                                                  options:0
                                                                    error:&error1];
                        UIImage *image = [UIImage imageWithData:imageData];
                        photo.thumbnail = image;
                        imageLoaded(index,image);
                        //photo.LargeImage = imageMidQ;
                        
                        //completionBlock(albumId,albumPhotos,nil);
                    });
                    
                    
                    counter++;
                    [albumPhotos addObject:photo];
                }
                
                completionBlock(albumId,albumPhotos,nil);
            }
        }
    });
}

-(void) createNewEvent:(NSString *) userID withEventName:(NSString *) eventName withEventStartTime: (NSString*) eventStartDateTime withEventEndTime:(NSString*) eventEndDateTime withPlaceLat:(NSString*) placeLat withPlaceLong:(NSString*) placeLong withLocationId: (NSString*) locationID completionBlock:(CreateNewEventCompletedBlock) createNewEventCompleted
{
    
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_async(queue, ^{
        
        NSArray *formFields = [NSArray arrayWithObjects:@"userID", @"eventName", @"eventStartDateTime", @"eventEndtDateTime", @"placeLat", @"placeLong", @"locationID", nil];
        NSArray *formValues = [NSArray arrayWithObjects:userID, eventName, eventStartDateTime, eventEndDateTime, placeLat,placeLong,locationID];
        NSDictionary *textParams = [NSDictionary dictionaryWithObjects:formValues forKeys:formFields];
        
        
        NSString *urlString = @"http://picaround.azurewebsites.net/Events/CreateNewEvent";
        NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
        [request setURL:[NSURL URLWithString:urlString]];
        [request setHTTPMethod:@"POST"];
        
        NSMutableData *body = [NSMutableData data];
        
        NSString *boundary = [NSString stringWithString:@"---------------------------14737809831466499882746641449"];
        NSString *contentType = [NSString stringWithFormat:@"multipart/form-data; boundary=%@", boundary];
        [request addValue:contentType forHTTPHeaderField:@"Content-Type"];
        
        
        // add the text form fields
        for (id key in textParams) {
            [body appendData:[[NSString stringWithFormat:@"--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
            [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"%@\"\r\n\r\n", key] dataUsingEncoding:NSUTF8StringEncoding]];
            [body appendData:[[NSString stringWithString:[textParams objectForKey:key]] dataUsingEncoding:NSUTF8StringEncoding]];
            [body appendData:[[NSString stringWithString:@"\r\n"] dataUsingEncoding:NSUTF8StringEncoding]];
        }
        
        // close the form
        [body appendData:[[NSString stringWithFormat:@"--%@--\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
        
        // set request body
        [request setHTTPBody:body];
        
        // send the request (submit the form) and get the response
        NSData *returnData = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
        NSString *returnString = [[NSString alloc] initWithData:returnData encoding:NSUTF8StringEncoding];
        
        
        NSLog(@"%@", returnString);
        createNewEventCompleted(@"OK");
        
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
