//
//  PicAroundAppDelegate.h
//  PicAround
//
//  Created by BenA on 2/6/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <FacebookSDK/FacebookSDK.h>
#import "PALoginViewController.h"
#import "TestFlight.h"
#import "GAI.h"

extern NSString *const FBSessionStateChangedNotification;

@interface PicAroundAppDelegate : UIResponder <UIApplicationDelegate>
@property (strong, nonatomic) PALoginViewController *viewController;

@property (strong, nonatomic) UIWindow *window;

@property (strong, nonatomic) NSDictionary<FBGraphUser> *user;
// FBSample logic
// In this sample the app delegate maintains a property for the current
// active session, and the view controllers reference the session via
// this property, as well as play a role in keeping the session object
// up to date; a more complicated application may choose to introduce
// a simple singleton that owns the active FBSession object as well
// as access to the object by the rest of the application
@property (strong, nonatomic) FBSession *session;
@property (strong, nonatomic) CLLocation *Location;
@property (strong, nonatomic) NSString *Latitude;
@property (strong, nonatomic) NSString *Longitude;
@property (strong, nonatomic) NSString *EventID;
@property (strong, nonatomic) NSString *EventName;
@property (strong, nonatomic) NSString *SelectedLocationName;

@property (strong, nonatomic) NSString *NewEventName;
@property (strong, nonatomic) NSString *NewEventLocationName;
@property (strong, nonatomic) NSString *Altitude;
@property (nonatomic) CLLocationDistance AltitudeDoubleValue;
@property (nonatomic) CLLocationDirection HeadingDoubleValue;
@property (strong, nonatomic) NSString *Heading;

@property (nonatomic) BOOL NewEventCreated;

@property BOOL DetailedViewSelected;


- (BOOL)openSessionWithAllowLoginUI:(BOOL)allowLoginUI;
-(void) sessionOpener;
- (void) closeSession;
@end
