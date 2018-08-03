//
//  PicAroundSecondViewController.m
//  PicAround
//
//  Created by BenA on 2/6/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import "PicAroundSecondViewController.h"
#import <QuartzCore/QuartzCore.h>

@implementation PicAroundSecondViewController

- (void)viewDidLoad
{
    if(![UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera])
    {
        Camera.hidden = YES;
    }
    _uploadImageProgress.progress = 0.0;
    canceled = NO;
    flashMode =  UIImagePickerControllerCameraFlashModeAuto;
    
}
-(void) viewDidAppear:(BOOL)animated
{
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
    cameraViewHeader.text = appDelegate.EventName;
    if ( canceled == NO) {
        [self getCameraPicture:self];
    }
    canceled = NO;
}

#pragma mark -
-(IBAction)getCameraPicture:(id)sender
{
    UIImagePickerController *picker = [[UIImagePickerController alloc]init];
    picker.delegate = self;
    picker.allowsEditing = NO;
    picker.sourceType = UIImagePickerControllerSourceTypeCamera;
    picker.cameraFlashMode = flashMode;
    //picker.cameraOverlayView = my view
    //picker.navigationItem.rightBarButtonItem.title = @"Upload";
    //picker.navigationBar.topItem.title = @"Upload";
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
    
    NSString *urlString = [NSString stringWithFormat:@"http://picaround.azurewebsites.net/Upload/UploadFile?lat=%@&lon=%@&alt=%@&userid=%@&eventid=%@&plcae=%@&eventLable=%@",appDelegate.Latitude,appDelegate.Longitude,@"0",appDelegate.user.id,@"1",@"testPlace",@"testEventlabel"];
    
    //    UIAlertView *alert = [[UIAlertView alloc]
    //                          initWithTitle: @"url is..."
    //                          message: urlString
    //                          delegate: nil
    //                          cancelButtonTitle:@"OK"
    //                          otherButtonTitles:nil];
    //    [alert show];
    
    [self presentModalViewController:picker animated:YES];
}

-(IBAction)selectExistingPicture{
    if([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypePhotoLibrary])
    {
        UIImagePickerController *picker = [[UIImagePickerController alloc]init];
        picker.delegate = self;
        picker.allowsEditing = NO;
        picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
        [self presentModalViewController:picker animated:YES];
    }
    else
    {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error accessing photo library"
                                                        message:@"Devices does not support a photo library"
                                                       delegate:nil
                                              cancelButtonTitle:@"Dismiss"
                                              otherButtonTitles:nil];
        [alert show];
    }
}
#pragma mark -
-(void) imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    [picker dismissModalViewControllerAnimated:YES];
    if (picker.sourceType == UIImagePickerControllerSourceTypeCamera) {
        flashMode = picker.cameraFlashMode;
    }

    UIImage *image = [info objectForKey:UIImagePickerControllerOriginalImage];
    
    self.imageView.image = image;
    self.imageView.alpha = 1;
    self.imageView.clipsToBounds = YES;
    self.imageView.layer.cornerRadius = 7.0;
    self.imageView.layer.masksToBounds = YES;
    self.imageView.layer.borderColor = [UIColor grayColor].CGColor;
    self.imageView.layer.borderWidth = 1.0;
    UIImageWriteToSavedPhotosAlbum(image, nil, nil, nil);
    
    
    
    NSData *imageData = UIImageJPEGRepresentation(self.imageView.image,0.5);
    
    _uploadImageProgress.progress = 0.0;
    _imageSize = imageData.length;
    
    [self uploadImage:imageData];
    [self getCameraPicture:self];
}

-(void) imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    canceled = YES;
    [picker dismissModalViewControllerAnimated:YES];
}

- (void)uploadImage:(NSData *) imageData {
	/*
	 turning the image into a NSData object
	 getting the image back out of the UIImageView
	 setting the quality to 90
     */
    
    
    
    
    
	
	// setting up the URL to post to
	//NSString *urlString = @"http://iphone.zcentric.com/test-upload.php";
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
    
	NSString *urlString = [NSString stringWithFormat:@"http://picaround.azurewebsites.net/Upload/UploadFile?lat=%@&lon=%@&alt=%@&heading=%@&userid=%@&eventid=%@&plcae=%@&eventLable=%@",appDelegate.Latitude,appDelegate.Longitude,appDelegate.Altitude,appDelegate.Heading,appDelegate.user.id,appDelegate.EventID,@"testPlace",@"testEventlabel"];
    
    
    
    
	// setting up the request object now
	NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
	[request setURL:[NSURL URLWithString:urlString]];
	[request setHTTPMethod:@"POST"];
	
	/*
	 add some header info now
	 we always need a boundary when we post a file
	 also we need to set the content type
	 
	 You might want to generate a random boundary.. this is just the same
	 as my output from wireshark on a valid html post
     */
	NSString *boundary = [NSString stringWithString:@"---------------------------14737809831466499882746641449"];
	NSString *contentType = [NSString stringWithFormat:@"multipart/form-data; boundary=%@",boundary];
	[request addValue:contentType forHTTPHeaderField: @"Content-Type"];
	
	/*
	 now lets create the body of the post
     */
	NSMutableData *body = [NSMutableData data];
	[body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];
	[body appendData:[[NSString stringWithString:@"Content-Disposition: form-data; name=\"userfile\"; filetype=\"image/jpeg\"; filename=\"ipodfile.jpg\"\r\n"] dataUsingEncoding:NSUTF8StringEncoding]];
	[body appendData:[[NSString stringWithString:@"Content-Type: application/octet-stream\r\n\r\n"] dataUsingEncoding:NSUTF8StringEncoding]];
	[body appendData:[NSData dataWithData:imageData]];
	[body appendData:[[NSString stringWithFormat:@"\r\n--%@--\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];
	// setting the body of the post to the reqeust
	[request setHTTPBody:body];
    [request setTimeoutInterval:30000];
	
    
    
    
    
	// now lets make the connection to the web
	//NSData *returnData = [NSURLConnection  sendSynchronousRequest:request returningResponse:nil error:nil];
    [NSURLConnection  connectionWithRequest: request delegate: self];
    
    
    
    
    
    
    
	//NSString *returnString = [[NSString alloc] initWithData:returnData encoding:NSUTF8StringEncoding];
	
    //
    //        alert = [[UIAlertView alloc]
    //                              initWithTitle: @"connectionWithRequest"
    //                              message: @"connectionWithRequest AFTER"
    //                              delegate: nil
    //                              cancelButtonTitle:@"OK"
    //                              otherButtonTitles:nil];
    //        [alert show];
    
    
    
	//NSLog(returnString);
}

#pragma mark -
- (void)connection:(NSURLConnection *)connection  didSendBodyData:(NSInteger)bytesWritten
 totalBytesWritten:(NSInteger)totalBytesWritten
totalBytesExpectedToWrite:(NSInteger)totalBytesExpectedToWrite
{
    dispatch_async(dispatch_get_main_queue(), ^{
        
        _uploadImageProgress.progress = ((totalBytesWritten/(float)_imageSize)*100)/100;
        
        
        NSString *totalBytesWrittenString = [NSString stringWithFormat:@"%i",totalBytesWritten];
        //    UIAlertView *alert = [[UIAlertView alloc]
        //                          initWithTitle: @"update"
        //                          message: totalBytesWrittenString
        //                          delegate: nil
        //                          cancelButtonTitle:@"OK"
        //                          otherButtonTitles:nil];
        //    [alert show];
    });
}

//- (void)uploadAsync:(NSData *) imageData completionBlock:(PAUploadCompletionBlock) completionBlock
//{
//
//
//    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
//
//    dispatch_async(queue, ^{
//        NSError *error = nil;
//        //NSString *queryUrl = [NSString stringWithContentsOfURL:[NSURL URLWithString:searchURL]
//         //                                             encoding:NSUTF8StringEncoding
//         //                                                error:&error];
//
//        dispatch_async(dispatch_get_main_queue(), ^{
//            UIAlertView *alert = [[UIAlertView alloc]
//                                  initWithTitle: @"uploadAsync"
//                                  message: @"uploadAsync started"
//                                  delegate: nil
//                                  cancelButtonTitle:@"OK"
//                                  otherButtonTitles:nil];
//            [alert show];
//        });
//
//
//        [self uploadImage:imageData];
//
//         completionBlock(@"done");
//
//
//
//    });
//
//}
-(void) navigationController:(UINavigationController*) navigationController willShowViewController:(UIViewController *)viewController animated:(BOOL)animated
{
//    UIBarButtonItem *uploadButton = [[UIBarButtonItem alloc] initWithTitle:@"Upload" style:UIBarButtonItemStylePlain target:self action:@selector(didFinishPickingMediaWithInfo)];
//    viewController.navigationController.navigationBar.topItem.title = @"Upload?";
//    [viewController.navigationController.navigationBar.topItem setRightBarButtonItem:uploadButton];
}

#pragma mark -
- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
    NSHTTPURLResponse* httpResponse = (NSHTTPURLResponse*) response;
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_async(queue, ^{
        
        //        dispatch_async(dispatch_get_main_queue(), ^{
        //                        UIAlertView *alert = [[UIAlertView alloc]
        //                                              initWithTitle: @"didReceiveResponse"
        //                                              message: [NSString stringWithFormat:@"%i",[httpResponse statusCode]]
        //                                              delegate: nil
        //                                              cancelButtonTitle:@"OK"
        //                                              otherButtonTitles:nil];
        //                        [alert show];
        //                    });
        
    });
}
#pragma mark -
- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
}
#pragma mark -
- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    //    UIAlertView *alert = [[UIAlertView alloc]
    //                          initWithTitle: @"Upload"
    //                  message: @"OK"
    //                delegate: nil
    //       cancelButtonTitle:@"OK"
    //      otherButtonTitles:nil];
    //    [alert show];
}
#pragma mark -
- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error;
{
    UIAlertView *alert = [[UIAlertView alloc]
                          initWithTitle: @"Upload"
                          message: @"FAIL"
                          delegate: nil
                          cancelButtonTitle:@"OK"
                          otherButtonTitles:nil];
    [alert show];
}
- (void)viewDidUnload {
    [self setUploadImageProgress:nil];
    cameraViewHeader = nil;
    [super viewDidUnload];
}
@end
