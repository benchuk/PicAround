//
//  PAProfileViewController.h
//  PicAround
//
//  Created by BenA on 2/13/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import <UIKit/UIKit.h> 
#import <FacebookSDK/FacebookSDK.h>
#import "GAI.h"
@interface PAProfileViewController : GAITrackedViewController
@property (strong, nonatomic) IBOutlet FBProfilePictureView *profileImage;
@property (weak, nonatomic) IBOutlet UILabel *userName;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *spinner;
@property (weak, nonatomic) IBOutlet UILabel *tempLocation;
- (IBAction)signout:(id)sender;

@end
