//
//  PAImageDetailController.h
//  PicAround
//
//  Created by BenA on 2/11/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <FacebookSDK/FacebookSDK.h>
#import <Social/Social.h>
#import <MessageUI/MessageUI.h>
#import "PAPhotoEntity.h"
@interface PAImageDetailController : UIViewController<UIActionSheetDelegate , MFMailComposeViewControllerDelegate , MFMessageComposeViewControllerDelegate>
{
    SLComposeViewController *slComposeViewController;
    UIActionSheet *sheet;
    
}
- (IBAction)share:(id)sender;

- (IBAction)openUserPage:(id)sender;
@property (weak) IBOutlet UIImageView * imageView;
@property (strong) IBOutlet UIImage* img;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *spinner;
@property (weak, nonatomic) IBOutlet PAPhotoEntity * PhotoEntity;
@property (weak, nonatomic) IBOutlet UILabel *userName;
@property (weak, nonatomic) IBOutlet UIImageView *detailsBackground;

@property (weak, nonatomic) IBOutlet UIImageView *profileImage;

@property (weak, nonatomic) IBOutlet UILabel *locationName;
@end
