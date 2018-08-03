//
//  PicAroundSecondViewController.h
//  PicAround
//
//  Created by BenA on 2/6/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PicAroundAppDelegate.h"

typedef void (^PAUploadCompletionBlock)(NSString *status);

@interface PicAroundSecondViewController : UIViewController<UIImagePickerControllerDelegate, NSURLConnectionDataDelegate, NSURLConnectionDelegate>
{

    __weak IBOutlet UILabel *cameraViewHeader;
    BOOL canceled;
    UIImagePickerControllerCameraFlashMode flashMode;

    IBOutlet UIButton *Camera;
}

- (void)connection:(NSURLConnection *)connection   didSendBodyData:(NSInteger)bytesWritten
 totalBytesWritten:(NSInteger)totalBytesWritten
totalBytesExpectedToWrite:(NSInteger)totalBytesExpectedToWrite;

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response;

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data;

- (void)connectionDidFinishLoading:(NSURLConnection *)connection;

-(IBAction)getCameraPicture:(id)sender;
-(IBAction)selectExistingPicture;

@property (weak, nonatomic) IBOutlet UIProgressView *uploadImageProgress;
@property (weak, nonatomic) IBOutlet UIImageView *imageView;
@property (nonatomic) NSUInteger imageSize;


@end

