//
//  PANewEventViewController.h
//  PicAround
//
//  Created by BenA on 2/23/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//
#import <FacebookSDK/FacebookSDK.h>
#import <UIKit/UIKit.h>
#import "GAI.h"
@interface PANewEventViewController : GAITrackedViewController
{
    IBOutlet UITextField *eventLocationTextField;
}
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *spinner;
@property (weak, nonatomic) IBOutlet UITextField *eventName;
@property (weak, nonatomic) IBOutlet UITextField *eventDuration;
@property (weak, nonatomic) IBOutlet UIDatePicker *eventStartDate;
- (IBAction)done:()sender;
@property (weak, nonatomic) IBOutlet UITextField *NewEventName;
- (IBAction)Cancel:(id)sender;
- (IBAction)SelectLocation:(id)sender;
@property (strong, nonatomic) FBCacheDescriptor *placeCacheDescriptor;

- (IBAction)loseFocus:(id)sender;

@property (strong, nonatomic) NSObject<FBGraphPlace> *selectedPlace;

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event;
- (IBAction)click:(id)sender;

@end
